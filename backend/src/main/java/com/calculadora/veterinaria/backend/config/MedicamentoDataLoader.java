package com.calculadora.veterinaria.backend.config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.calculadora.veterinaria.backend.entity.Medicamento;
import com.calculadora.veterinaria.backend.repository.MedicamentoRepository;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(2)
public class MedicamentoDataLoader implements CommandLineRunner {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Override
    public void run(String... args) throws Exception {
        List<String> nomes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("medicamentos.csv").getInputStream(), StandardCharsets.UTF_8))) {
            reader.readLine();

            String linha;
            while ((linha = reader.readLine()) != null) {
                nomes.add(linha);
            }
        }

        for (String nome : nomes) {
            if (!medicamentoRepository.existsByNome(nome)) {
                Medicamento m = new Medicamento();
                m.setNome(nome);
                medicamentoRepository.save(m);
            }
        }
    }
}
