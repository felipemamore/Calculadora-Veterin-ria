package com.calculadora.veterinaria.backend.service;
import com.calculadora.veterinaria.backend.dto.GroqRequest;
import com.calculadora.veterinaria.backend.dto.GroqResponse;
import com.calculadora.veterinaria.backend.repository.AlimentoToxicoEspecieRepository;
import com.calculadora.veterinaria.backend.repository.DosagemRepository;
import com.calculadora.veterinaria.backend.repository.MedicacaoToxicaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AIService {

    @Autowired
    private DosagemRepository dosagemRepository;
    @Autowired
    private AlimentoToxicoEspecieRepository alimentoToxicoEspecieRepository;
    @Autowired
    private MedicacaoToxicaRepository medicacaoToxicaRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${groq.api.key}")
    private String apiKey;
    @Value("${groq.model.name}")
    private String modelName;

    public String getResponse(String userQuestion) {
        String url = "https://api.groq.com/openai/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // Monta o prompt
        String dataContext = buildDataContext();

        String systemPrompt = String.format(
            "Você é um assistente veterinário prestativo. Responda a pergunta do usuário baseando-se estritamente nos dados de referência a seguir. " +
            "Não forneça conselhos médicos. Se a informação não estiver nos dados, diga que não possui essa informação. DADOS: \n%s",
        dataContext
    );

        // Cria o corpo da requisição no formato Chat Completions
        GroqRequest requestBody = new GroqRequest(
            this.modelName, // https://console.groq.com/docs/deprecations
            systemPrompt,
            userQuestion
        );

        HttpEntity<GroqRequest> entity = new HttpEntity<>(requestBody, headers);

        // Faz a chamada POST
        GroqResponse response = restTemplate.postForObject(url, entity, GroqResponse.class);

        // Extrai o texto da resposta
        if (response != null && !response.getChoices().isEmpty()) {
            return response.getChoices().get(0).getMessage().getContent();
        }

        return "Não foi possível obter uma resposta da IA.";
    }



    private String buildDataContext() {
    StringBuilder context = new StringBuilder();

    // 1. Adiciona dados de Dosagens
    context.append("--- DADOS DE DOSAGEM ---\n");
    dosagemRepository.findAll().forEach(dosagem -> {
        context.append(String.format(
            "Medicamento %s para %s em apresentação '%s': Dose recomendada de %.2f mg/kg e concentração de %.2f mg/mL.\n",
            dosagem.getMedicamento().getNome(),
            dosagem.getEspecie().getNome(),
            dosagem.getApresentacao(),
            dosagem.getDoseRecomendadaMgPorKg(),
            dosagem.getConcentracaoMgPorMl()
        ));
    });

    // 2. Adiciona dados de Alimentos Tóxicos
    context.append("\n--- DADOS DE ALIMENTOS TÓXICOS ---\n");
    alimentoToxicoEspecieRepository.findAll().forEach(relacao -> {
        context.append(String.format(
            "Alimento %s é tóxico para %s. Descrição: %s\n",
            relacao.getAlimento().getNome(),
            relacao.getEspecie().getNome(),
            relacao.getDescricao()
        ));
    });

    // 3. Adiciona dados de Medicações Tóxicas
    context.append("\n--- DADOS DE MEDICAÇÕES TÓXICAS ---\n");
    medicacaoToxicaRepository.findAll().forEach(relacao -> {
        context.append(String.format(
            "Medicamento %s é considerado tóxico para %s.\n",
            relacao.getMedicamento().getNome(),
            relacao.getEspecie().getNome()
        ));
    });

    return context.toString();
}
}