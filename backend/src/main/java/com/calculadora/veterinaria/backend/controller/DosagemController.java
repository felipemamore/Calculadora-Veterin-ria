package com.calculadora.veterinaria.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.calculadora.veterinaria.backend.entity.Dosagem;
import com.calculadora.veterinaria.backend.repository.DosagemRepository;

@RestController
@RequestMapping("/api/dosagem")
public class DosagemController {

    @Autowired
    private DosagemRepository dosagemRepository;

    @GetMapping
    public ResponseEntity<Dosagem> buscarDosagem(
        @RequestParam Long medicamentoId,
        @RequestParam Long especieId) {
        
        Dosagem dosagem = dosagemRepository.findByMedicamentoIdAndEspecieId(medicamentoId, especieId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dosagem não encontrada"));
        
        return ResponseEntity.ok(dosagem);
    }
}
