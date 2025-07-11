package com.calculadora.veterinaria.backend.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.calculadora.veterinaria.backend.dto.EspecieDTO;
import com.calculadora.veterinaria.backend.entity.Especie;
import com.calculadora.veterinaria.backend.repository.EspecieRepository;
@RestController
@RequestMapping("/api/especie")
public class EspecieController {

    @Autowired
    private EspecieRepository especieRepository;

    @GetMapping
    public ResponseEntity<EspecieDTO> buscarPorNome(@RequestParam String nome) {
        Especie especie = especieRepository.findByNomeIgnoreCase(nome)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Espécie não encontrada"));

        EspecieDTO dto = new EspecieDTO();
        dto.setId(especie.getId());
        dto.setNome(especie.getNome());

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<EspecieDTO>> listarTodos() {
        List<EspecieDTO> lista = especieRepository.findAll().stream()
         .map(e -> new EspecieDTO(e.getId(), e.getNome()))
        .toList();

    return ResponseEntity.ok(lista);
}
}
