package com.calculadora.veterinaria.backend.config;

import com.calculadora.veterinaria.backend.entity.Dosagem;
import com.calculadora.veterinaria.backend.entity.Especie;
import com.calculadora.veterinaria.backend.entity.Medicamento;
import com.calculadora.veterinaria.backend.repository.DosagemRepository;
import com.calculadora.veterinaria.backend.repository.EspecieRepository;
import com.calculadora.veterinaria.backend.repository.MedicamentoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class DataLoader implements CommandLineRunner {

    private final EspecieRepository especieRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final DosagemRepository dosagemRepository;

    public DataLoader(EspecieRepository especieRepository,
                      MedicamentoRepository medicamentoRepository,
                      DosagemRepository dosagemRepository) {
        this.especieRepository = especieRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.dosagemRepository = dosagemRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (dosagemRepository.count() == 0) {
            loadDataFromCsv();
        }
    }

    private void loadDataFromCsv() throws IOException, CsvValidationException {
        try (InputStream is = getClass().getResourceAsStream("/dados_dosagens.csv");
     CSVReader reader = new CSVReaderBuilder(new InputStreamReader(is, StandardCharsets.UTF_8))
             .withSkipLines(1)
             .build()) {

    String[] line;
    while ((line = reader.readNext()) != null) {
        String doseString = line[2];
        String concentracaoString = line[3];
        if (doseString.equals("-") || concentracaoString.equals("-")) {
            continue;
        }

        String nomeMedicamento = line[0];
        String nomeEspecie = line[1];
        double dose = Double.parseDouble(doseString.replace(",", "."));
        double concentracao = Double.parseDouble(concentracaoString.replace(",", "."));

        Medicamento medicamento = medicamentoRepository.findByNomeIgnoreCase(nomeMedicamento)
                .orElseGet(() -> {
                    Medicamento novo = new Medicamento();
                    novo.setNome(nomeMedicamento);
                    return medicamentoRepository.save(novo);
                });


        Especie especie = especieRepository.findByNomeIgnoreCase(nomeEspecie)
                .orElseGet(() -> {
                    Especie nova = new Especie();
                    nova.setNome(nomeEspecie);
                    return especieRepository.save(nova);
                });

        Dosagem dosagem = new Dosagem();
        dosagem.setMedicamento(medicamento);
        dosagem.setEspecie(especie);
        dosagem.setDoseRecomendadaMgPorKg(dose);
        dosagem.setConcentracaoMgPorMl(concentracao);
        dosagemRepository.save(dosagem);
    }
}
        System.out.println("Carga de dados via CSV finalizada.");
    }
}
