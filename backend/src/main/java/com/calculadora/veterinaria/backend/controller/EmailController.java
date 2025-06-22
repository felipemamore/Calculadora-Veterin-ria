package com.calculadora.veterinaria.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.calculadora.veterinaria.backend.service.EmailService;
import java.util.Map;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/api/teste-email")
    public String enviarEmail(@RequestParam String para) {
        emailService.enviarEmail(
            para,
            "Teste de e-mail",
            "Este é um e-mail de teste enviado com Spring Boot e Mailtrap."
        );
        return "E-mail enviado!";
    }

    @PostMapping("/api/teste-email")
public ResponseEntity<String> enviarEmailPost(@RequestBody Map<String, String> body) {
    String destinatario = body.get("para");
    try {
        emailService.enviarEmail(
            destinatario,
            "Teste de e-mail",
            "Este é um e-mail de teste enviado com Spring Boot e Mailtrap."
        );
        return ResponseEntity.ok("E-mail enviado!");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Erro ao enviar e-mail: " + e.getMessage());
    }
}
}