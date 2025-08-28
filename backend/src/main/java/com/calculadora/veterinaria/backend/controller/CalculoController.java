package com.calculadora.veterinaria.backend.controller;

import com.calculadora.veterinaria.backend.dto.CalculoDTO;
import com.calculadora.veterinaria.backend.dto.DoseRequest;
import com.calculadora.veterinaria.backend.dto.DoseResponse;
import com.calculadora.veterinaria.backend.entity.Calculo;
import com.calculadora.veterinaria.backend.service.CalculoService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculo")
public class CalculoController {

    @Autowired
    private CalculoService calculoService;

    @PostMapping("/dose")
    public ResponseEntity<DoseResponse> calcularDose(@RequestBody DoseRequest request) {
        DoseResponse resposta = calculoService.calcularDose(request);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/historico")
    public ResponseEntity<List<CalculoDTO>> buscarHistoricoDoUsuarioLogado() {
    // 1. Esta parte está perfeita. Você busca a lista de entidades do banco.
    List<Calculo> calculos = calculoService.buscarUltimosCalculosDoUsuario();

    // 2. Aqui está a conversão correta de Entidade para DTO.
    List<CalculoDTO> dtos = calculos.stream()
            .map(calculo -> new CalculoDTO(
                calculo.getDosagem().getMedicamento().getNome(),

                calculo.getDataHora()
            ))
            .collect(Collectors.toList());

    return ResponseEntity.ok(dtos);
}
}
