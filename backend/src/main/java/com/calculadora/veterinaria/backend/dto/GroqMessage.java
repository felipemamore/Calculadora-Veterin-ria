package com.calculadora.veterinaria.backend.dto;

public class GroqMessage {
    private String role;
    private String content;

    public GroqMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
    // Getters
    public String getRole() { return role; }
    public String getContent() { return content; }
}