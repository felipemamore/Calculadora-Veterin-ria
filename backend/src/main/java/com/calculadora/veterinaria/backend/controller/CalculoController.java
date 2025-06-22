package com.calculadora.veterinaria.backend.controller;

import com.calculadora.veterinaria.backend.dto.DoseRequest;
import com.calculadora.veterinaria.backend.dto.DoseResponse;
import com.calculadora.veterinaria.backend.service.CalculoService;
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
}
