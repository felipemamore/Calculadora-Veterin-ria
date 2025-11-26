package com.calculadora.veterinaria.backend.controller;

import com.calculadora.veterinaria.backend.entity.Dosagem;
import com.calculadora.veterinaria.backend.repository.DosagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dosagem")
public class DosagemController {

    @Autowired
    private DosagemRepository dosagemRepository;
    @GetMapping("/apresentacoes")
    public ResponseEntity<List<String>> getApresentacoes(
            @RequestParam Long medicamentoId,
            @RequestParam Long especieId
    ) {
        List<Dosagem> dosagens = dosagemRepository.findByMedicamentoIdAndEspecieId(medicamentoId, especieId);
        List<String> apresentacoes = dosagens.stream()
                .map(Dosagem::getApresentacao)
                .distinct()
                .collect(Collectors.toList());
        
        if (apresentacoes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(apresentacoes);
    }

    @GetMapping
    public ResponseEntity<Dosagem> getDosagem(
            @RequestParam Long medicamentoId,
            @RequestParam Long especieId,
            @RequestParam String apresentacao
    ) {
        return dosagemRepository.findByMedicamentoIdAndEspecieIdAndApresentacaoIgnoreCase(medicamentoId, especieId, apresentacao)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}