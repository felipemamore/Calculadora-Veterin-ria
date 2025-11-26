package com.calculadora.veterinaria.backend.config;

import com.calculadora.veterinaria.backend.entity.DosagemSilvestre;
import com.calculadora.veterinaria.backend.repository.DosagemSilvestreRepository;
import com.opencsv.CSVParser;       
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class SilvestreDataLoader implements CommandLineRunner {

    private final DosagemSilvestreRepository repository;

    public SilvestreDataLoader(DosagemSilvestreRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            loadData();
        }
    }

    private void loadData() {
        try (InputStream is = getClass().getResourceAsStream("/dosagens_silvestres.csv")) {
            
            if (is == null) {
                System.out.println("Arquivo CSV de Silvestres não encontrado!");
                return;
            }

            // 1. Criar o Parser com o separador correto
            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(',') // Seu CSV usa vírgula
                    .build();

            // 2. Criar o Reader usando o Parser
            try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .withSkipLines(1)      // Pula o cabeçalho
                    .withCSVParser(parser) // Usa o parser configurado acima
                    .build()) {

                String[] line;
                while ((line = reader.readNext()) != null) {
                    // Validação simples para evitar linhas vazias
                    if (line.length < 3) continue;

                    DosagemSilvestre d = new DosagemSilvestre();
                    d.setMedicamento(line[0].trim());
                    d.setGrupo(line[1].trim());

                    // Tratamento de número (troca vírgula por ponto se necessário)
                    String doseStr = line[2].replace(",", ".");
                    try {
                        d.setDoseMgKg(Double.parseDouble(doseStr));
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao ler dose para: " + d.getMedicamento());
                        continue;
                    }

                    if (line.length > 3) {
                        d.setAcao(line[3].trim());
                    } else {
                        d.setAcao("");
                    }

                    repository.save(d);
                }
                System.out.println("Dados de Silvestres carregados com sucesso!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}