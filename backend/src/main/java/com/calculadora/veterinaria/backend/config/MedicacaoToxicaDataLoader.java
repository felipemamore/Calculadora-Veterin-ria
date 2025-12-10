package com.calculadora.veterinaria.backend.config;

import com.calculadora.veterinaria.backend.entity.Especie;
import com.calculadora.veterinaria.backend.entity.MedicacaoToxica;
import com.calculadora.veterinaria.backend.entity.Medicamento;
import com.calculadora.veterinaria.backend.repository.EspecieRepository;
import com.calculadora.veterinaria.backend.repository.MedicacaoToxicaRepository;
import com.calculadora.veterinaria.backend.repository.MedicamentoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Order(3)
public class MedicacaoToxicaDataLoader {

    @Autowired
    private EspecieRepository especieRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private MedicacaoToxicaRepository medicacaoToxicaRepository;

    @PostConstruct
    public void carregarMedicacoesToxicas() {
        Map<String, List<String>> toxicasPorEspecie = new HashMap<>();
        toxicasPorEspecie.put("Canina", List.of("Meloxicam", "Dipirona", "Furosemida", "Ciprofloxacino", "Adrenalina"));
        toxicasPorEspecie.put("Felina", List.of("Meloxicam", "Dipirona", "Enrofloxacina", "Tramal", "Quetamina"));
        toxicasPorEspecie.put("Equina", List.of("Adrenalina", "Borgal", "Lidocaina"));
        toxicasPorEspecie.put("Bovino", List.of("Ciprofloxacino", "Shotapen", "Dexametasona"));
        toxicasPorEspecie.put("Mamíferos", List.of("Meloxicam", "Metronidazol", "Diazepam"));
        toxicasPorEspecie.put("Aves", List.of("Metronidazol", "Meloxicam", "Enrofloxacina"));
        toxicasPorEspecie.put("Répteis", List.of("Meloxicam", "Ceftriaxona", "Metadona"));
        toxicasPorEspecie.put("Roedores", List.of("Dipirona", "Enrofloxacina", "Amoxilina"));

        System.out.println("Iniciando verificação de Medicações Tóxicas...");

        for (Map.Entry<String, List<String>> entry : toxicasPorEspecie.entrySet()) {
            String nomeEspecie = entry.getKey();
            List<String> nomesMedicamentos = entry.getValue();

            Optional<Especie> especieOpt = especieRepository.findByNomeIgnoreCase(nomeEspecie);
            if (especieOpt.isEmpty()) {
                System.out.println("⚠️ Espécie não encontrada: " + nomeEspecie);
                continue; 
            }
            Especie especie = especieOpt.get();

            for (String nomeMed : nomesMedicamentos) {
                // Tenta achar o remédio, se não achar, CRIA UM NOVO (Resiliência)
                Medicamento medicamento = medicamentoRepository.findByNomeIgnoreCase(nomeMed)
                    .orElseGet(() -> {
                        System.out.println("Criando medicamento faltante: " + nomeMed);
                        Medicamento novo = new Medicamento();
                        novo.setNome(nomeMed);
                        return medicamentoRepository.save(novo);
                    });

                // Só salva se ainda não existir essa relação específica
                if (!medicacaoToxicaRepository.existsByEspecieAndMedicamento(especie, medicamento)) {
                    MedicacaoToxica toxica = new MedicacaoToxica();
                    toxica.setEspecie(especie);
                    toxica.setMedicamento(medicamento);
                    medicacaoToxicaRepository.save(toxica);
                    System.out.println("✅ Nova toxicidade cadastrada: " + nomeMed + " para " + nomeEspecie);
                }
            }
        }
        System.out.println("Verificação de Medicações Tóxicas concluída.");
    }
}