package com.calculadora.veterinaria.backend.dto;

public class MedicamentoDTO {
    private Long id;
    private String nome;

    public MedicamentoDTO() {
    }

    public MedicamentoDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
}

    public String getNome() {
        return nome;
}

    public void setId(Long id) {
        this.id = id;
}

    public void setNome(String nome) {
        this.nome = nome;
}
    
}
