package com.calculadora.veterinaria.backend.config;

import com.calculadora.veterinaria.backend.entity.Dosagem;
import com.calculadora.veterinaria.backend.entity.Especie;
import com.calculadora.veterinaria.backend.entity.Medicamento;
import com.calculadora.veterinaria.backend.repository.DosagemRepository;
import com.calculadora.veterinaria.backend.repository.EspecieRepository;
import com.calculadora.veterinaria.backend.repository.MedicamentoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
        // Inserir espécies
        String[] especies = {"Canina", "Felina", "Bovino", "Mamíferos", "Aves", "Répteis", "Roedores", "Equina"};
        Map<String, Especie> especieMap = new HashMap<>();

        for (String nome : especies) {
            Especie e = especieRepository.findByNomeIgnoreCase(nome).orElseGet(() -> {
                Especie nova = new Especie();
                nova.setNome(nome);
                return especieRepository.save(nova);
            });
            especieMap.put(nome, e);
        }

        // Inserir medicamentos
        String[] nomesMedicamentos = {
            "Acebrofilina", "Acetato de dexametasona", "Albenza (Albendazol)", "Amoxicilina",
            "Ansiolítico (Diazepam)", "Apomorfina", "Avermectina", "Betametasona",
            "Bupivacaína", "Butorfanol", "Captopril", "Carprofeno", "Cefalexina",
            "Ceftriaxona", "Cloridrato de tramadol", "Cloranfenicol", "Ciprofloxacina",
            "Clindamicina", "Dantroleno"
        };
        Map<String, Medicamento> medicamentoMap = new HashMap<>();

        for (String nome : nomesMedicamentos) {
            Medicamento m = medicamentoRepository.findByNomeIgnoreCase(nome).orElseGet(() -> {
                Medicamento novo = new Medicamento();
                novo.setNome(nome);
                return medicamentoRepository.save(novo);
            });
            medicamentoMap.put(nome, m);
        }

// Acebrofilina
salvarDosagem(medicamentoMap, especieMap, "Acebrofilina", "Canina", 15.0, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Acebrofilina", "Felina", 12.0, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Acebrofilina", "Equina", 10.0, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Acebrofilina", "Bovino", 8.0, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Acebrofilina", "Mamíferos", 13.0, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Acebrofilina", "Aves", 10.0, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Acebrofilina", "Répteis", 9.0, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Acebrofilina", "Roedores", 11.0, 25.0);

// Acetato de dexametasona
salvarDosagem(medicamentoMap, especieMap, "Acetato de dexametasona", "Canina", 0.2, 4.0);
salvarDosagem(medicamentoMap, especieMap, "Acetato de dexametasona", "Felina", 0.25, 4.0);
salvarDosagem(medicamentoMap, especieMap, "Acetato de dexametasona", "Equina", 0.15, 4.0);
salvarDosagem(medicamentoMap, especieMap, "Acetato de dexametasona", "Bovino", 0.1, 4.0);
salvarDosagem(medicamentoMap, especieMap, "Acetato de dexametasona", "Mamíferos", 0.2, 4.0);
salvarDosagem(medicamentoMap, especieMap, "Acetato de dexametasona", "Aves", 0.05, 4.0);
salvarDosagem(medicamentoMap, especieMap, "Acetato de dexametasona", "Répteis", 0.07, 4.0);
salvarDosagem(medicamentoMap, especieMap, "Acetato de dexametasona", "Roedores", 0.09, 4.0);

// Albenza (Albendazol)
salvarDosagem(medicamentoMap, especieMap, "Albenza (Albendazol)", "Canina", 25.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Albenza (Albendazol)", "Felina", 20.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Albenza (Albendazol)", "Equina", 10.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Albenza (Albendazol)", "Bovino", 7.5, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Albenza (Albendazol)", "Mamíferos", 22.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Albenza (Albendazol)", "Aves", 12.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Albenza (Albendazol)", "Répteis", 10.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Albenza (Albendazol)", "Roedores", 15.0, 50.0);

// Amoxicilina
salvarDosagem(medicamentoMap, especieMap, "Amoxicilina", "Canina", 10.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Amoxicilina", "Felina", 12.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Amoxicilina", "Equina", 15.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Amoxicilina", "Bovino", 8.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Amoxicilina", "Mamíferos", 11.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Amoxicilina", "Aves", 9.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Amoxicilina", "Répteis", 8.5, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Amoxicilina", "Roedores", 10.0, 50.0);

// Ansiolítico (Diazepam)
salvarDosagem(medicamentoMap, especieMap, "Ansiolítico (Diazepam)", "Canina", 0.5, 5.0);
salvarDosagem(medicamentoMap, especieMap, "Ansiolítico (Diazepam)", "Felina", 0.3, 5.0);
salvarDosagem(medicamentoMap, especieMap, "Ansiolítico (Diazepam)", "Equina", 0.25, 5.0);
salvarDosagem(medicamentoMap, especieMap, "Ansiolítico (Diazepam)", "Bovino", 0.2, 5.0);
salvarDosagem(medicamentoMap, especieMap, "Ansiolítico (Diazepam)", "Mamíferos", 0.4, 5.0);
salvarDosagem(medicamentoMap, especieMap, "Ansiolítico (Diazepam)", "Aves", 0.1, 5.0);
salvarDosagem(medicamentoMap, especieMap, "Ansiolítico (Diazepam)", "Répteis", 0.15, 5.0);
salvarDosagem(medicamentoMap, especieMap, "Ansiolítico (Diazepam)", "Roedores", 0.2, 5.0);

// Apomorfina
salvarDosagem(medicamentoMap, especieMap, "Apomorfina", "Canina", 0.05, 1.0);
salvarDosagem(medicamentoMap, especieMap, "Apomorfina", "Felina", 0.03, 1.0);
salvarDosagem(medicamentoMap, especieMap, "Apomorfina", "Equina", 0.02, 1.0);
salvarDosagem(medicamentoMap, especieMap, "Apomorfina", "Bovino", 0.015, 1.0);
salvarDosagem(medicamentoMap, especieMap, "Apomorfina", "Mamíferos", 0.04, 1.0);
salvarDosagem(medicamentoMap, especieMap, "Apomorfina", "Aves", 0.01, 1.0);
salvarDosagem(medicamentoMap, especieMap, "Apomorfina", "Répteis", 0.012, 1.0);
salvarDosagem(medicamentoMap, especieMap, "Apomorfina", "Roedores", 0.02, 1.0);

// Avermectina
salvarDosagem(medicamentoMap, especieMap, "Avermectina", "Canina", 0.006, 1.0);
salvarDosagem(medicamentoMap, especieMap, "Avermectina", "Felina", 0.004, 1.0);
salvarDosagem(medicamentoMap, especieMap, "Avermectina", "Equina", 0.002, 1.0);
salvarDosagem(medicamentoMap, especieMap, "Avermectina", "Bovino", 0.002, 1.0);
salvarDosagem(medicamentoMap, especieMap, "Avermectina", "Mamíferos", 0.005, 1.0);
salvarDosagem(medicamentoMap, especieMap, "Avermectina", "Aves", 0.001, 1.0);
salvarDosagem(medicamentoMap, especieMap, "Avermectina", "Répteis", 0.0015, 1.0);
salvarDosagem(medicamentoMap, especieMap, "Avermectina", "Roedores", 0.002, 1.0);

// Betametasona
salvarDosagem(medicamentoMap, especieMap, "Betametasona", "Felina", 0.25, 4.0);
salvarDosagem(medicamentoMap, especieMap, "Betametasona", "Equina", 0.15, 4.0);
salvarDosagem(medicamentoMap, especieMap, "Betametasona", "Bovino", 0.1, 4.0);
salvarDosagem(medicamentoMap, especieMap, "Betametasona", "Mamíferos", 0.2, 4.0);
salvarDosagem(medicamentoMap, especieMap, "Betametasona", "Aves", 0.05, 4.0);
salvarDosagem(medicamentoMap, especieMap, "Betametasona", "Répteis", 0.07, 4.0);
salvarDosagem(medicamentoMap, especieMap, "Betametasona", "Roedores", 0.09, 4.0);

// Bupivacaína
salvarDosagem(medicamentoMap, especieMap, "Bupivacaína", "Canina", 2.0, 5.0);
salvarDosagem(medicamentoMap, especieMap, "Bupivacaína", "Felina", 1.5, 5.0);
salvarDosagem(medicamentoMap, especieMap, "Bupivacaína", "Equina", 1.0, 5.0);
salvarDosagem(medicamentoMap, especieMap, "Bupivacaína", "Bovino", 0.8, 5.0);
salvarDosagem(medicamentoMap, especieMap, "Bupivacaína", "Mamíferos", 1.8, 5.0);
salvarDosagem(medicamentoMap, especieMap, "Bupivacaína", "Aves", 0.5, 5.0);
salvarDosagem(medicamentoMap, especieMap, "Bupivacaína", "Répteis", 0.6, 5.0);
salvarDosagem(medicamentoMap, especieMap, "Bupivacaína", "Roedores", 1.0, 5.0);

// Butorfanol
salvarDosagem(medicamentoMap, especieMap, "Butorfanol", "Canina", 0.2, 10.0);
salvarDosagem(medicamentoMap, especieMap, "Butorfanol", "Felina", 0.3, 10.0);
salvarDosagem(medicamentoMap, especieMap, "Butorfanol", "Equina", 0.1, 10.0);
salvarDosagem(medicamentoMap, especieMap, "Butorfanol", "Bovino", 0.1, 10.0);
salvarDosagem(medicamentoMap, especieMap, "Butorfanol", "Mamíferos", 0.25, 10.0);
salvarDosagem(medicamentoMap, especieMap, "Butorfanol", "Aves", 0.05, 10.0);
salvarDosagem(medicamentoMap, especieMap, "Butorfanol", "Répteis", 0.08, 10.0);
salvarDosagem(medicamentoMap, especieMap, "Butorfanol", "Roedores", 0.15, 10.0);

// Captopril
salvarDosagem(medicamentoMap, especieMap, "Captopril", "Canina", 0.5, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Captopril", "Felina", 0.4, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Captopril", "Equina", 0.3, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Captopril", "Bovino", 0.2, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Captopril", "Mamíferos", 0.45, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Captopril", "Aves", 0.1, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Captopril", "Répteis", 0.15, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Captopril", "Roedores", 0.3, 25.0);

// Carprofeno
salvarDosagem(medicamentoMap, especieMap, "Carprofeno", "Canina", 4.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Carprofeno", "Felina", 2.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Carprofeno", "Equina", 1.5, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Carprofeno", "Bovino", 1.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Carprofeno", "Mamíferos", 3.5, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Carprofeno", "Aves", 0.8, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Carprofeno", "Répteis", 1.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Carprofeno", "Roedores", 2.0, 50.0);

// Ceftriaxona
salvarDosagem(medicamentoMap, especieMap, "Ceftriaxona", "Canina", 25.0, 100.0);
salvarDosagem(medicamentoMap, especieMap, "Ceftriaxona", "Felina", 20.0, 100.0);
salvarDosagem(medicamentoMap, especieMap, "Ceftriaxona", "Equina", 15.0, 100.0);
salvarDosagem(medicamentoMap, especieMap, "Ceftriaxona", "Bovino", 10.0, 100.0);
salvarDosagem(medicamentoMap, especieMap, "Ceftriaxona", "Mamíferos", 18.0, 100.0);
salvarDosagem(medicamentoMap, especieMap, "Ceftriaxona", "Aves", 5.0, 100.0);
salvarDosagem(medicamentoMap, especieMap, "Ceftriaxona", "Répteis", 7.0, 100.0);
salvarDosagem(medicamentoMap, especieMap, "Ceftriaxona", "Roedores", 12.0, 100.0);

// Cloridrato de tramadol
salvarDosagem(medicamentoMap, especieMap, "Cloridrato de tramadol", "Canina", 4.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Cloridrato de tramadol", "Felina", 2.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Cloridrato de tramadol", "Equina", 1.5, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Cloridrato de tramadol", "Bovino", 1.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Cloridrato de tramadol", "Mamíferos", 3.5, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Cloridrato de tramadol", "Aves", 0.8, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Cloridrato de tramadol", "Répteis", 1.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Cloridrato de tramadol", "Roedores", 2.0, 50.0);

// Cloranfenicol
salvarDosagem(medicamentoMap, especieMap, "Cloranfenicol", "Canina", 50.0, 100.0);
salvarDosagem(medicamentoMap, especieMap, "Cloranfenicol", "Felina", 40.0, 100.0);
salvarDosagem(medicamentoMap, especieMap, "Cloranfenicol", "Equina", 30.0, 100.0);
salvarDosagem(medicamentoMap, especieMap, "Cloranfenicol", "Bovino", 20.0, 100.0);
salvarDosagem(medicamentoMap, especieMap, "Cloranfenicol", "Mamíferos", 45.0, 100.0);
salvarDosagem(medicamentoMap, especieMap, "Cloranfenicol", "Aves", 10.0, 100.0);
salvarDosagem(medicamentoMap, especieMap, "Cloranfenicol", "Répteis", 15.0, 100.0);
salvarDosagem(medicamentoMap, especieMap, "Cloranfenicol", "Roedores", 25.0, 100.0);

// Ciprofloxacina
salvarDosagem(medicamentoMap, especieMap, "Ciprofloxacina", "Canina", 10.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Ciprofloxacina", "Felina", 12.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Ciprofloxacina", "Equina", 8.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Ciprofloxacina", "Bovino", 6.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Ciprofloxacina", "Mamíferos", 9.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Ciprofloxacina", "Aves", 3.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Ciprofloxacina", "Répteis", 4.0, 50.0);
salvarDosagem(medicamentoMap, especieMap, "Ciprofloxacina", "Roedores", 6.0, 50.0);

// Clindamicina
salvarDosagem(medicamentoMap, especieMap, "Clindamicina", "Canina", 11.0, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Clindamicina", "Felina", 10.0, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Clindamicina", "Equina", 8.0, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Clindamicina", "Bovino", 6.0, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Clindamicina", "Mamíferos", 9.0, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Clindamicina", "Aves", 2.5, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Clindamicina", "Répteis", 3.0, 25.0);
salvarDosagem(medicamentoMap, especieMap, "Clindamicina", "Roedores", 5.0, 25.0);

//Dantroleno
salvarDosagem(medicamentoMap, especieMap, "Dantroleno", "Canina", 2.0, 10.0);
salvarDosagem(medicamentoMap, especieMap, "Dantroleno", "Felina", 2.5, 10.0);
salvarDosagem(medicamentoMap, especieMap, "Dantroleno", "Equina", 4.0, 10.0);
salvarDosagem(medicamentoMap, especieMap, "Dantroleno", "Bovino", 3.5, 10.0);
salvarDosagem(medicamentoMap, especieMap, "Dantroleno", "Mamíferos", 2.0, 10.0);
salvarDosagem(medicamentoMap, especieMap, "Dantroleno", "Aves", 1.5, 10.0);
salvarDosagem(medicamentoMap, especieMap, "Dantroleno", "Répteis", 1.0, 10.0);
salvarDosagem(medicamentoMap, especieMap, "Dantroleno", "Roedores", 1.2, 10.0);


    }

    private void salvarDosagem(Map<String, Medicamento> medicamentoMap,
                               Map<String, Especie> especieMap,
                               String nomeMedicamento, String nomeEspecie,
                               Double dose, Double concentracao) {
        Medicamento medicamento = medicamentoMap.get(nomeMedicamento);
        Especie especie = especieMap.get(nomeEspecie);
        if (medicamento != null && especie != null) {
            boolean exists = dosagemRepository.findByMedicamentoIdAndEspecieId(medicamento.getId(), especie.getId()).isPresent();
            if (!exists) {
                Dosagem dosagem = new Dosagem();
                dosagem.setMedicamento(medicamento);
                dosagem.setEspecie(especie);
                dosagem.setDoseRecomendadaMgPorKg(dose);
                dosagem.setConcentracaoMgPorMl(concentracao);
                dosagemRepository.save(dosagem);
            }
        }
    }
}
