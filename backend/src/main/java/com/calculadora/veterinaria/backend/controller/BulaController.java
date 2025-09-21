package com.calculadora.veterinaria.backend.controller;

import com.calculadora.veterinaria.backend.dto.BulaDTO;
import com.calculadora.veterinaria.backend.service.BulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/bula")
public class BulaController {

    @Autowired
    private BulaService bulaService;

    @GetMapping
    public ResponseEntity<BulaDTO> buscarBula(@RequestParam("nome") String medicamentoNome) {
        BulaDTO bula = bulaService.findBulaByNome(medicamentoNome)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bula não encontrada"));
        return ResponseEntity.ok(bula);
    }
}