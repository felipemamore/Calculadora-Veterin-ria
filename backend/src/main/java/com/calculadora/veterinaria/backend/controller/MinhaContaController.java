package com.calculadora.veterinaria.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.calculadora.veterinaria.backend.dto.DadosUsuarioDTO;
import com.calculadora.veterinaria.backend.entity.Usuario;
import com.calculadora.veterinaria.backend.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/conta-do-usuario")
public class MinhaContaController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
public ResponseEntity<DadosUsuarioDTO> getMinhaConta(@AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 1. Busca o usuário completo no banco de dados usando o e-mail (que é o username)
    Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));


    DadosUsuarioDTO dto = new DadosUsuarioDTO(usuario);

    return ResponseEntity.ok(dto);
    }
}

