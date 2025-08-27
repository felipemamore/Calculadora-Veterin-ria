package com.calculadora.veterinaria.backend.dto;

import com.calculadora.veterinaria.backend.entity.Usuario;

public class DadosUsuarioDTO {
    private String nome;
    private String email;

    public DadosUsuarioDTO(Usuario usuario) {
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
