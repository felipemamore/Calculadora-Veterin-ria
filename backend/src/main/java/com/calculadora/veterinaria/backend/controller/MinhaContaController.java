package com.calculadora.veterinaria.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.calculadora.veterinaria.backend.dto.MinhaContaDTO;
import com.calculadora.veterinaria.backend.entity.Usuario;
import com.calculadora.veterinaria.backend.repository.DosagemRepository;
import com.calculadora.veterinaria.backend.service.MinhaContaService;

@RestController
@RequestMapping("/api/conta-do-usuario")
public class MinhaContaController {
    @Autowired
    private DosagemRepository dosagemRepository;
    @Autowired
    private MinhaContaService minhaContaService;

    public ResponseEntity<MinhaContaDTO> getMinhaConta(@AuthenticationPrincipal Usuario usuario) {
        if (usuario == null) {
            // Se esta linha for alcançada em um endpoint authenticated(), algo está errado com a segurança.
            return ResponseEntity.status(401).build(); 
        }
        MinhaContaDTO dto = minhaContaService.obterDadosMinhaConta(usuario);
        return ResponseEntity.ok(dto);
    }
}
