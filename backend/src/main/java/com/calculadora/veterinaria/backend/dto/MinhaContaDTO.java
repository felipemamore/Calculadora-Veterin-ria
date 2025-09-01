package com.calculadora.veterinaria.backend.dto;

import java.util.List;
import com.calculadora.veterinaria.backend.entity.Usuario;

public class MinhaContaDTO {
    private String nome;
    private String email;

    public MinhaContaDTO(Usuario usuario, List<CalculoResponse> historico) {
        this.nome = usuario.getNomeCompleto();
        this.email = usuario.getEmail();
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
}
