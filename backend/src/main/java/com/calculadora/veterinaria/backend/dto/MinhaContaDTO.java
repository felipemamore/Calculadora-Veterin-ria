package com.calculadora.veterinaria.backend.dto;

import java.util.List;
import com.calculadora.veterinaria.backend.entity.Usuario;

public class MinhaContaDTO {
    private String nome;
    private String email;
    private List<CalculoResponse> historico;

    public MinhaContaDTO(Usuario usuario, List<CalculoResponse> historico) {
        this.nome = usuario.getNomeCompleto();
        this.email = usuario.getEmail();
        this.historico = historico;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<CalculoResponse> getHistorico() {
        return historico;
    }

    public void setHistorico(List<CalculoResponse> historico) {
        this.historico = historico;
    }
}
