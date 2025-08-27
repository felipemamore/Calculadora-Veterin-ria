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
        // Se não houver dosagens, carrega os dados do CSV.
        // É uma verificação mais simples que garante que o processo só rode uma vez.
        if (dosagemRepository.count() == 0) {
            loadDataFromCsv();
        }
    }

    private void loadDataFromCsv() throws IOException, CsvValidationException {
        // Usa try-with-resources para garantir que o leitor de arquivos seja fechado
        try (InputStream is = getClass().getResourceAsStream("/dados_dosagens.csv");
             CSVReader reader = new CSVReaderBuilder(new InputStreamReader(is, StandardCharsets.UTF_8))
                     .withSkipLines(1) // Pula a linha do cabeçalho
                     .build()) {

            String[] line;
            while ((line = reader.readNext()) != null) {
                String nomeMedicamento = line[0];
                String nomeEspecie = line[1];
                double dose = Double.parseDouble(line[2]);
                double concentracao = Double.parseDouble(line[3]);

                // Busca o medicamento ou cria um novo se não existir.
                // Exatamente a lógica que você já usava, mas de forma mais dinâmica.
                Medicamento medicamento = medicamentoRepository.findByNomeIgnoreCase(nomeMedicamento)
                        .orElseGet(() -> {
                            Medicamento novo = new Medicamento();
                            novo.setNome(nomeMedicamento);
                            return medicamentoRepository.save(novo);
                        });

                // Busca a espécie ou cria uma nova se não existir.
                Especie especie = especieRepository.findByNomeIgnoreCase(nomeEspecie)
                        .orElseGet(() -> {
                            Especie nova = new Especie();
                            nova.setNome(nomeEspecie);
                            return especieRepository.save(nova);
                        });

                // Cria e salva a dosagem, associando o medicamento e a espécie.
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
