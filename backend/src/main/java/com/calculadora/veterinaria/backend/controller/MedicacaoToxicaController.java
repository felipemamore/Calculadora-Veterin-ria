package com.calculadora.veterinaria.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.calculadora.veterinaria.backend.entity.MedicacaoToxica;
import com.calculadora.veterinaria.backend.repository.MedicacaoToxicaRepository;

@RestController
@RequestMapping("/api/toxicas")
public class MedicacaoToxicaController {

    @Autowired
    private MedicacaoToxicaRepository repository;

    @GetMapping
    public ResponseEntity<List<String>> buscarPorEspecie(@RequestParam String especie) {
        List<MedicacaoToxica> medicamentosToxicos = repository.findByEspecieNomeIgnoreCase(especie);
        
        List<String> nomesMedicamentos = medicamentosToxicos.stream()
            .map(mt -> mt.getMedicamento().getNome())  // Pega o nome do medicamento relacionado
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(nomesMedicamentos);
    }
}

