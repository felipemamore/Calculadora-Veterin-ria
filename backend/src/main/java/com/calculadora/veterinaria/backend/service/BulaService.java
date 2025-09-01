package com.calculadora.veterinaria.backend.service;

import com.calculadora.veterinaria.backend.dto.BulaDTO;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class BulaService {

    private final Map<String, BulaDTO> bulas = new HashMap<>();

    @PostConstruct
    private void carregarDadosDasBulas() {
        try (InputStream is = getClass().getResourceAsStream("/bulas.csv");
             InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                BulaDTO bula = new BulaDTO();
                String nome = line[0];
                
                bula.setNome(nome);
                bula.setIndicacoes(line[1]);
                bula.setContraIndicacoes(line[2]);
                bula.setEfeitosAdversos(line[3]);
                bula.setReproducao(line[4]);
                bula.setSuperdosagem(line[5]);
                bula.setFarmacodinamica(line[6]);
                bula.setFarmacocinetica(line[7]);
                bula.setMonitoramento(line[8]);

                bulas.put(nome, bula);
                
                if ("Adrenalina".equalsIgnoreCase(nome)) {
                    bulas.put("Epinefrina", bula);
                }
            }
            System.out.println("Carga de dados de " + bulas.size() + " bulas via CSV finalizada.");

        } catch (Exception e) {
            System.err.println("Erro ao carregar o arquivo bulas.csv");
            e.printStackTrace();
        }
    }

    public Optional<BulaDTO> findBulaByNome(String nome) {
        for (String key : bulas.keySet()) {
            if (key.equalsIgnoreCase(nome)) {
                return Optional.of(bulas.get(key));
            }
        }
        return Optional.empty();
    }
}