package com.calculadora.veterinaria.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.calculadora.veterinaria.backend.entity.Medicamento;
import com.calculadora.veterinaria.backend.repository.MedicamentoRepository;

import java.util.Arrays;
import java.util.List;

@Component
public class MedicamentoDataLoader implements CommandLineRunner {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Override
    public void run(String... args) throws Exception {
        List<String> nomes = Arrays.asList(
            "Acebrofilina",
            "Acetato de dexametasona",
            "Albenza (Albendazol)",
            "Amoxicilina",
            "Ansiolítico (Diazepam)",
            "Apomorfina",
            "Avermectina",
            "Betametasona",
            "Bupivacaína",
            "Butorfanol",
            "Captopril",
            "Carprofeno",
            "Cefalexina",
            "Ceftriaxona",
            "Cloridrato de tramadol",
            "Cloranfenicol",
            "Ciprofloxacina",
            "Clindamicina",
            "Dantroleno"
        );

        for (String nome : nomes) {
            if (!medicamentoRepository.existsByNome(nome)) {
                Medicamento m = new Medicamento();
                m.setNome(nome);
                // Você pode deixar as outras propriedades nulas ou setar valores padrão
                medicamentoRepository.save(m);
            }
        }
    }
}
