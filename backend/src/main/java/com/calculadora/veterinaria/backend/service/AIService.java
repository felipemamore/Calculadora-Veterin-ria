package com.calculadora.veterinaria.backend.service;
import com.calculadora.veterinaria.backend.dto.GroqRequest;
import com.calculadora.veterinaria.backend.dto.GroqResponse;
import com.calculadora.veterinaria.backend.repository.AlimentoToxicoEspecieRepository;
import com.calculadora.veterinaria.backend.repository.DosagemRepository;
import com.calculadora.veterinaria.backend.repository.DosagemSilvestreRepository;
import com.calculadora.veterinaria.backend.repository.MedicacaoToxicaRepository;

import java.util.concurrent.atomic.AtomicBoolean;

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
    private DosagemSilvestreRepository dosagemSilvestreRepository;

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
        String dataContext = buildDataContext(userQuestion);

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
        try {
            GroqResponse response = restTemplate.postForObject(url, entity, GroqResponse.class);

            if (response != null && !response.getChoices().isEmpty()) {
                return response.getChoices().get(0).getMessage().getContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao processar a resposta da IA (Limite de tokens ou erro de conexão). Tente ser mais específico.";
        }

        return "Não foi possível obter uma resposta da IA.";
    }



    private String buildDataContext(String question) {
    StringBuilder context = new StringBuilder();
    String termoBusca = question.toLowerCase();

    AtomicBoolean hasData = new AtomicBoolean(false);

    // 1. Adiciona dados de Dosagens
    context.append("--- DADOS DE DOSAGEM ---\n");
    dosagemRepository.findAll().forEach(dosagem -> {
        String nomeMed = dosagem.getMedicamento().getNome().toLowerCase();
            String nomeEsp = dosagem.getEspecie().getNome().toLowerCase();
            
            // Só adiciona se o nome do remédio ou da espécie estiver na pergunta
            if (termoBusca.contains(nomeMed) || termoBusca.contains(nomeEsp)) {
                context.append(String.format(
                    "Med: %s | Esp: %s | Apr: %s | Dose: %.2f mg/kg | Conc: %.2f mg/mL\n", // Formato compactado
                    dosagem.getMedicamento().getNome(),
                    dosagem.getEspecie().getNome(),
                    dosagem.getApresentacao(),
                    dosagem.getDoseRecomendadaMgPorKg(),
                    dosagem.getConcentracaoMgPorMl()
                ));
                hasData.set(true);
            }
        });

    // 2. Adiciona dados de Alimentos Tóxicos
    if (termoBusca.contains("tóxico") || termoBusca.contains("toxico") || termoBusca.contains("comer") || termoBusca.contains("alimento")) {
            context.append("\n--- DADOS DE ALIMENTOS TÓXICOS ---\n");
            alimentoToxicoEspecieRepository.findAll().forEach(relacao -> {
                String nomeAlimento = relacao.getAlimento().getNome().toLowerCase();
                if (termoBusca.contains(nomeAlimento) || termoBusca.contains("quais")) {
                    context.append(String.format(
                        "%s é tóxico para %s. %s\n",
                        relacao.getAlimento().getNome(),
                        relacao.getEspecie().getNome(),
                        relacao.getDescricao()
                    ));
                }
            });
        }

    // 3. Adiciona dados de Medicações Tóxicas
    if (termoBusca.contains("tóxi") || termoBusca.contains("toxi") || 
        termoBusca.contains("faz mal") || termoBusca.contains("proibido") || 
        termoBusca.contains("perigo") || termoBusca.contains("lista") || 
        termoBusca.contains("quais")) {
        
        context.append("\n--- DADOS DE MEDICAÇÕES TÓXICAS ---\n");
        
        medicacaoToxicaRepository.findAll().forEach(relacao -> {
            String nomeMed = relacao.getMedicamento().getNome().toLowerCase();
            String nomeEsp = relacao.getEspecie().getNome().toLowerCase();
            
            if (termoBusca.contains(nomeMed) || 
                termoBusca.contains(nomeEsp) || 
                termoBusca.contains("quais") || 
                termoBusca.contains("lista") ||
                termoBusca.contains("tóxi") ||
                termoBusca.contains("toxi")) {
                
                context.append(String.format(
                    "- %s é estritamente PROIBIDO/TÓXICO para %s.\n",
                    relacao.getMedicamento().getNome(),
                    relacao.getEspecie().getNome()
                ));
            }
        });
    }

    // 4. Adiciona dados de Dosagens para Silvestres
    context.append("\n--- DADOS SILVESTRES ---\n");
        dosagemSilvestreRepository.findAll().forEach(silvestre -> {
            String nomeMed = silvestre.getMedicamento().toLowerCase();
            String grupo = silvestre.getGrupo().toLowerCase();
            
            if (termoBusca.contains(nomeMed) || termoBusca.contains(grupo) || termoBusca.contains("silvestre")) {
                context.append(String.format(
                    "Med: %s | Grupo: %s | Dose: %.2f mg/kg\n",
                    silvestre.getMedicamento(),
                    silvestre.getGrupo(),
                    silvestre.getDoseMgKg()
                ));
            }
        });

    context.append("\n--- REGRAS DE CÁLCULO PARA SILVESTRES (ALOMETRIA) ---\n");
    context.append("Para animais silvestres, o cálculo não é linear. Usamos Alometria baseada na TMB (Taxa Metabólica Basal).\n");
    context.append("Fórmula da TMB: K * (Peso em kg)^0.75\n");
    context.append("Fórmula da Dose Total (mg): Dose da Tabela * (TMB / 10)\n");
    context.append("Constantes K utilizadas:\n");
    context.append("- Répteis e Anfíbios: K = 10\n");
    context.append("- Marsupiais: K = 49\n");
    context.append("- Mamíferos: K = 70\n");
    context.append("- Aves (Não-Passeriformes): K = 78\n");
    context.append("- Aves (Passeriformes): K = 129\n");

    context.append("\n--- REGRAS DE CÁLCULO DE NECESSIDADE ENERGÉTICA (NED/DER) ---\n");
    context.append("A fórmula é: DER = RER * Fator\n");
    context.append("Onde RER (Necessidade Energética de Repouso) = 70 * (Peso em kg)^0.75\n");
    context.append("Fatores para CÃES:\n");
    context.append("- Em crescimento: Fator = 3.0\n");
    context.append("- Adultos com atividade moderada: Fator = 1.8\n");
    context.append("- Em lactação: Fator = 5.6\n");
    context.append("- Adultos sedentários e/ou idosos: Fator = 1.4\n");
    context.append("Fatores para GATOS:\n");
    context.append("- Filhotes em crescimento: Fator = 2.5\n");
    context.append("- Adultos ativos e/ou gestantes: Fator = 1.4\n");
    context.append("- Em lactação: Fator = 3.0\n");
    context.append("- Adultos sedentários e/ou idosos: Fator = 1.2\n");
    context.append("Ao calcular, mostre o resultado final em Kcal/dia.\n");
    context.append("\n--- REGRAS DE CÁLCULO DE GESTAÇÃO ---\n");
    context.append("A duração da gestação para Cães (Canina) é de 62 a 64 dias.\n");
    context.append("A duração da gestação para Gatos (Felina) é de 60 a 65 dias.\n");
    context.append("Ao calcular, forneça a data mínima e a data máxima do parto com base na data de início fornecida.\n");

    return context.toString();
}
}