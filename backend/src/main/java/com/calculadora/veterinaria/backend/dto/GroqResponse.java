package com.calculadora.veterinaria.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroqResponse {
    private List<GroqChoice> choices;
    // Getter e Setter
    public List<GroqChoice> getChoices() { return choices; }
    public void setChoices(List<GroqChoice> choices) { this.choices = choices; }
}