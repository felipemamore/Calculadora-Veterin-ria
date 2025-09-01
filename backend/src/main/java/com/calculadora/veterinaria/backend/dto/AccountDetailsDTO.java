package com.calculadora.veterinaria.backend.dto;

import com.calculadora.veterinaria.backend.entity.Usuario;

// Este DTO envia os dados para o frontend
public class AccountDetailsDTO {
    private String nomeCompleto;
    private String email;
    private String cpf;
    private String rg;
    private String crmv;
    private String ocupacao;

    public AccountDetailsDTO(Usuario usuario) {
        this.nomeCompleto = usuario.getNomeCompleto();
        this.email = usuario.getEmail();
        this.cpf = usuario.getCpf();
        this.rg = usuario.getRg();
        this.crmv = usuario.getCrmv();
        this.ocupacao = usuario.getOcupacao();
    }
    
    // Getters
    public String getNomeCompleto() { return nomeCompleto; }
    public String getEmail() { return email; }
    public String getCpf() { return cpf; }
    public String getRg() { return rg; }
    public String getCrmv() { return crmv; }
    public String getOcupacao() { return ocupacao; }
}