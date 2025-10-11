package com.calculadora.veterinaria.backend.controller;

import com.calculadora.veterinaria.backend.dto.QuestionDTO;
import com.calculadora.veterinaria.backend.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/ask")
    public ResponseEntity<String> askAssistant(@RequestBody QuestionDTO questionDTO) {
        try {
            String response = aiService.getResponse(questionDTO.getQuestion());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erro ao processar a solicitação de IA.");
        }
    }
}