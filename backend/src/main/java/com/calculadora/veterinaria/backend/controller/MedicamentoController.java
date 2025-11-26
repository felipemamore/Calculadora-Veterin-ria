package com.calculadora.veterinaria.backend.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.calculadora.veterinaria.backend.dto.MedicamentoDTO;
import com.calculadora.veterinaria.backend.entity.Medicamento;
import com.calculadora.veterinaria.backend.repository.MedicamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/medicamentos")
public class MedicamentoController {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @GetMapping
    public ResponseEntity<MedicamentoDTO> buscarPorNome(@RequestParam String nome) {
        Medicamento medicamento = medicamentoRepository.findByNomeIgnoreCase(nome)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicamento não encontrado"));

        MedicamentoDTO dto = new MedicamentoDTO();
        dto.setId(medicamento.getId());
        dto.setNome(medicamento.getNome());

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<MedicamentoDTO>> listarTodos() {
        List<MedicamentoDTO> lista = medicamentoRepository.findAll().stream()
            .map(m -> new MedicamentoDTO(m.getId(), m.getNome()))
            .toList();

        return ResponseEntity.ok(lista);
}
}

