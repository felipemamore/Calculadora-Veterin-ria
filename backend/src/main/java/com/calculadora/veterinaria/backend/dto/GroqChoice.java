package com.calculadora.veterinaria.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroqChoice {
    private GroqMessage message;
    // Getter e Setter
    public GroqMessage getMessage() { return message; }
    public void setMessage(GroqMessage message) { this.message = message; }
}