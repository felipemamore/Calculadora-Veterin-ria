package com.calculadora.veterinaria.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CalculoDTO {
    private String nome;
    private LocalDateTime data;

    public CalculoDTO(){

    }

    public CalculoDTO(String nome, LocalDateTime data){
        this.nome = nome;
        this.data = data;
    }
    
    public String getNome(){
        return nome;
    }

    public LocalDateTime getData(){
        return data;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setData(LocalDateTime data){
        this.data = data;
    }

}
