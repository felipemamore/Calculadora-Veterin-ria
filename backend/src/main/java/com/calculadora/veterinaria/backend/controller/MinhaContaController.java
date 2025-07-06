package com.calculadora.veterinaria.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.calculadora.veterinaria.backend.dto.CalculoResponse;
import com.calculadora.veterinaria.backend.dto.MinhaContaDTO;
import com.calculadora.veterinaria.backend.entity.Calculo;
import com.calculadora.veterinaria.backend.entity.Usuario;
import com.calculadora.veterinaria.backend.repository.CalculoRepository;
import com.calculadora.veterinaria.backend.repository.DosagemRepository;
import com.calculadora.veterinaria.backend.repository.UsuarioRepository;
import com.calculadora.veterinaria.backend.service.MinhaContaService;

@RestController
@RequestMapping("/api/conta-do-usuario")
public class MinhaContaController {
    @Autowired
    private DosagemRepository dosagemRepository;
    @Autowired
    private MinhaContaService minhaContaService;

    @Autowired
    private CalculoRepository calculoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/ultimos-calculos")
    public ResponseEntity<List<CalculoResponse>> getUltimosCalculos(@AuthenticationPrincipal Usuario usuario) {
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Calculo> ultimosCalculos = calculoRepository.findTop5ByUsuarioOrderByDataHoraDesc(usuario);
        List<CalculoResponse> response = ultimosCalculos.stream()
            .map(CalculoResponse::new)
            .toList();
        return ResponseEntity.ok(response);
    }

        @GetMapping
        public ResponseEntity<MinhaContaDTO> getMinhaConta(@AuthenticationPrincipal UserDetails userDetails) {
            if (userDetails == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        List<Calculo> historico = calculoRepository.findTop5ByUsuarioOrderByDataHoraDesc(usuario);
        List<CalculoResponse> resposta = historico.stream()
        .map(CalculoResponse::new)
        .toList();

        MinhaContaDTO dto = new MinhaContaDTO(usuario, resposta);
    return ResponseEntity.ok(dto);
    }
}

