package com.calculadora.veterinaria.backend.config;

import com.calculadora.veterinaria.backend.entity.Alimento;
import com.calculadora.veterinaria.backend.entity.AlimentoToxicoEspecie;
import com.calculadora.veterinaria.backend.entity.Especie;
import com.calculadora.veterinaria.backend.repository.AlimentoRepository;
import com.calculadora.veterinaria.backend.repository.AlimentoToxicoEspecieRepository;
import com.calculadora.veterinaria.backend.repository.EspecieRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Component
public class AlimentosToxicosDataLoader {

    @Autowired private EspecieRepository especieRepository;
    @Autowired private AlimentoRepository alimentoRepository;
    @Autowired private AlimentoToxicoEspecieRepository alimentoToxicoEspecieRepository;

    @PostConstruct
    public void carregarDados() {
        if (alimentoToxicoEspecieRepository.count() > 0) return;
        try (InputStream is = getClass().getResourceAsStream("/alimentos-toxicos.csv")) {
            if (is == null) {
                throw new IOException("Arquivo 'alimentos-toxicos.csv' não encontrado no classpath.");
            }

            Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            String csvContent = FileCopyUtils.copyToString(reader);

            String[] lines = csvContent.split("\\r?\\n");
            
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (line.trim().isEmpty()) continue;

                String[] columns = line.split(";", 3);
                if (columns.length < 3) continue;

                String nomeAlimento = columns[0].trim();
                String descricao = columns[1].trim().replace("\"", "");
                String nomeEspecie = columns[2].trim();

                Especie especie = especieRepository.findByNomeIgnoreCase(nomeEspecie).orElse(null);
                if (especie == null) continue;

                Alimento alimento = alimentoRepository.findByNomeIgnoreCase(nomeAlimento)
                    .orElseGet(() -> {
                        Alimento novoAlimento = new Alimento();
                        novoAlimento.setNome(nomeAlimento);
                        return alimentoRepository.save(novoAlimento);
                    });

                AlimentoToxicoEspecie relacaoToxica = new AlimentoToxicoEspecie();
                relacaoToxica.setAlimento(alimento);
                relacaoToxica.setEspecie(especie);
                relacaoToxica.setDescricao(descricao);
                alimentoToxicoEspecieRepository.save(relacaoToxica);
            }

        } catch (IOException e) {
            throw new RuntimeException("Falha ao ler o arquivo CSV de alimentos tóxicos", e);
        }
    }
}