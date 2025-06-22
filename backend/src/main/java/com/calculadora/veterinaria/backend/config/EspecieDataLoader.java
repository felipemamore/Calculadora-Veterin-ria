package com.calculadora.veterinaria.backend.config;

import com.calculadora.veterinaria.backend.entity.Especie;
import com.calculadora.veterinaria.backend.repository.EspecieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class EspecieDataLoader implements CommandLineRunner {

    @Autowired
    private EspecieRepository especieRepository;

    @Override
    public void run(String... args) throws Exception {
        String[] especies = {
            "Canina",
            "Felina",
            "Equina",
            "Bovino",
            "Mamíferos",
            "Aves",
            "Répteis",
            "Roedores"
        };

        for (String nome : especies) {
            if (!especieRepository.existsByNome(nome)) {
                Especie e = new Especie();
                e.setNome(nome);
                especieRepository.save(e);
            }
        }
    }
}
