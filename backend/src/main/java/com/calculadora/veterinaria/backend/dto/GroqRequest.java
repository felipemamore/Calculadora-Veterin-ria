package com.calculadora.veterinaria.backend.dto;

import java.util.List;

public class GroqRequest {
    private String model;
    private List<GroqMessage> messages;

    public GroqRequest(String model, String systemPrompt, String userQuestion) {
        this.model = model;
        this.messages = List.of(
            new GroqMessage("system", systemPrompt),
            new GroqMessage("user", userQuestion)
        );
    }
    // Getters
    public String getModel() { return model; }
    public List<GroqMessage> getMessages() { return messages; }
}