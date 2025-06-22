package com.calculadora.veterinaria.backend.controller;

import com.calculadora.veterinaria.backend.dto.EmailRequest;
import com.calculadora.veterinaria.backend.service.RecuperacaoSenhaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/senha")
public class RecuperacaoSenhaController {

    private final RecuperacaoSenhaService service;

    public RecuperacaoSenhaController(RecuperacaoSenhaService service) {
        this.service = service;
    }

    @PostMapping("/forgot-password")
    
    public ResponseEntity<?> solicitarResetSenha(@RequestBody EmailRequest emailRequest) {
    
    String email = emailRequest.getEmail();

    if (email == null || email.isBlank()) {
        return ResponseEntity.badRequest().body(Map.of("message", "E-mail inválido."));
    }

    boolean enviado = service.solicitarRedefinicaoSenha(email);

    if (enviado) {
        return ResponseEntity.ok(Map.of("message", "E-mail de recuperação enviado com sucesso."));
    } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "E-mail não encontrado"));
    }
}

    @PostMapping("/reset-password")
    public ResponseEntity<?> redefinirSenha(@RequestParam("token") String token,
                                        @RequestBody Map<String, String> body) {
    String novaSenha = body.get("novaSenha");

    if (novaSenha == null || novaSenha.trim().isEmpty()) {
        return ResponseEntity.badRequest().body("A nova senha é obrigatória.");
    }

    boolean sucesso = service.redefinirSenha(token, novaSenha);
    System.out.println("Token recebido na redefinição de senha: " + token);

    if (sucesso) {
        return ResponseEntity.ok("Senha redefinida com sucesso.");
    } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("Token inválido ou expirado.");
    }
}

}
