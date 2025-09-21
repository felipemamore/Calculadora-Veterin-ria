package com.calculadora.veterinaria.backend.dto;

public class AlimentoToxicoDTO {
    private Long id;
    private String nome;
    private String descricao;

    public AlimentoToxicoDTO() {
    }


    public AlimentoToxicoDTO(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

     public AlimentoToxicoDTO(Long id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}