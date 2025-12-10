package com.calculadora.veterinaria.backend.controller;

import com.calculadora.veterinaria.backend.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.lang.reflect.Method;

@RestController
public class DebugController {

    @Autowired
    private com.calculadora.veterinaria.backend.repository.MedicacaoToxicaRepository medicacaoToxicaRepository;
    @Autowired
    private com.calculadora.veterinaria.backend.repository.MedicamentoRepository medicamentoRepository;
    @Autowired
    private com.calculadora.veterinaria.backend.repository.EspecieRepository especieRepository;

    @Autowired
    private AIService aiService;

    @GetMapping("/api/debug/context")
    public String verContextoDaIA(@RequestParam String pergunta) {
        try {
            // Como o método buildDataContext é private, vamos usar reflexão para "espiar" ele
            // (Apenas para debug, não faça isso em produção comercial!)
            Method method = AIService.class.getDeclaredMethod("buildDataContext", String.class);
            method.setAccessible(true);
            String contexto = (String) method.invoke(aiService, pergunta);
            
            return "<html><body><h1>O que a IA está lendo:</h1><pre>" + contexto + "</pre></body></html>";
        } catch (Exception e) {
            return "Erro ao ler contexto: " + e.getMessage();
        }
    }
    @GetMapping("/api/debug/forcar-cadastro")
    public String forcarCadastro() {
        try {
            StringBuilder log = new StringBuilder();

            // 1. Busca ou Cria Adrenalina
            var adrenalina = medicamentoRepository.findByNomeIgnoreCase("Adrenalina")
                .orElseGet(() -> {
                    var m = new com.calculadora.veterinaria.backend.entity.Medicamento();
                    m.setNome("Adrenalina");
                    return medicamentoRepository.save(m);
                });

            // 2. Lista de espécies onde ela é tóxica (Segundo seu DataLoader)
            // Nota: Adrenalina geralmente é usada em emergência, mas vamos seguir sua regra de negócio de marcá-la como tóxica/perigosa.
            String[] especiesToxicas = {"Canina", "Equina"}; 

            for (String nomeEsp : especiesToxicas) {
                var especieOpt = especieRepository.findByNomeIgnoreCase(nomeEsp);
                
                if (especieOpt.isPresent()) {
                    var especie = especieOpt.get();
                    
                    if (!medicacaoToxicaRepository.existsByEspecieAndMedicamento(especie, adrenalina)) {
                        var tox = new com.calculadora.veterinaria.backend.entity.MedicacaoToxica();
                        tox.setEspecie(especie);
                        tox.setMedicamento(adrenalina);
                        medicacaoToxicaRepository.save(tox);
                        log.append("✅ Adrenalina marcada como tóxica para: ").append(nomeEsp).append("<br>");
                    } else {
                        log.append("⚠️ Já existia para: ").append(nomeEsp).append("<br>");
                    }
                } else {
                    log.append("❌ Espécie não encontrada: ").append(nomeEsp).append("<br>");
                }
            }
            
            return "<html><body><h1>Resultado da Força Bruta:</h1>" + log.toString() + "</body></html>";

        } catch (Exception e) {
            return "Erro crítico: " + e.getMessage();
        }
    }
}