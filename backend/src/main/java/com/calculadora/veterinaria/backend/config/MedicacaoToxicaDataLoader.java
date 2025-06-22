package com.calculadora.veterinaria.backend.config;

import com.calculadora.veterinaria.backend.entity.Especie;
import com.calculadora.veterinaria.backend.entity.MedicacaoToxica;
import com.calculadora.veterinaria.backend.entity.Medicamento;
import com.calculadora.veterinaria.backend.repository.EspecieRepository;
import com.calculadora.veterinaria.backend.repository.MedicacaoToxicaRepository;
import com.calculadora.veterinaria.backend.repository.MedicamentoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MedicacaoToxicaDataLoader {

    @Autowired
    private EspecieRepository especieRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private MedicacaoToxicaRepository medicacaoToxicaRepository;

    @PostConstruct
    public void carregarMedicacoesToxicas() {
        if (medicacaoToxicaRepository.count() > 0) return;

        Map<String, List<String>> toxicasPorEspecie = new HashMap<>();
        toxicasPorEspecie.put("Canina", List.of("Paracetamol", "Ibuprofeno", "Captopril", "Ciprofloxacina", "Cafeína"));
        toxicasPorEspecie.put("Felina", List.of("Paracetamol", "Ibuprofeno", "Cefalexina", "Cloridrato de tramadol", "Cafeína"));
        toxicasPorEspecie.put("Equina", List.of("Cafeína", "Avermectina", "Bupivacaína"));
        toxicasPorEspecie.put("Bovino", List.of("Ciprofloxacina", "Dantroleno", "Betametasona"));
        toxicasPorEspecie.put("Mamíferos", List.of("Ibuprofeno", "Cloranfenicol", "Ansiolítico (Diazepam)"));
        toxicasPorEspecie.put("Aves", List.of("Albenza (Albendazol)", "Carprofeno", "Clindamicina"));
        toxicasPorEspecie.put("Répteis", List.of("Ibuprofeno", "Ceftriaxona", "Apomorfina"));
        toxicasPorEspecie.put("Roedores", List.of("Paracetamol", "Avermectina", "Cefalexina"));

        for (Map.Entry<String, List<String>> entry : toxicasPorEspecie.entrySet()) {
            String nomeEspecie = entry.getKey();
            List<String> nomesMedicamentos = entry.getValue();

            Optional<Especie> especieOpt = especieRepository.findByNomeIgnoreCase(nomeEspecie);
            if (especieOpt.isEmpty()) continue;

            Especie especie = especieOpt.get();

            for (String nomeMed : nomesMedicamentos) {
                Optional<Medicamento> medicamentoOpt = medicamentoRepository.findByNomeIgnoreCase(nomeMed);
                if (medicamentoOpt.isEmpty()) continue;

                Medicamento medicamento = medicamentoOpt.get();

                // Evita duplicatas
                boolean existe = medicacaoToxicaRepository.existsByEspecieAndMedicamento(especie, medicamento);
                if (!existe) {
                    MedicacaoToxica toxica = new MedicacaoToxica();
                    toxica.setEspecie(especie);
                    toxica.setMedicamento(medicamento);
                    medicacaoToxicaRepository.save(toxica);
                }
            }
        }
    }
}
