package com.calculadora.veterinaria.backend.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCompleto;
    private String rg;
    private String cpf;
    private String email;
    private String crmv;
    private String ocupacao;
    private String avatarUrl;

    @NotBlank
    private String senha;

    // Getters e Setters personalizados
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getCrmv() { return crmv; }
    public void setCrmv(String crmv) { this.crmv = crmv; }

    public String getOcupacao() { return ocupacao; }
    public void setOcupacao(String ocupacao) { this.ocupacao = ocupacao; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) {this.avatarUrl = avatarUrl; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}