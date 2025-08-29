package com.calculadora.veterinaria.backend.dto;
import java.time.LocalDateTime;

public class CalculoDTO {
    private String nome;
    private LocalDateTime data;
    private String especie;

    public CalculoDTO(){

    }

    public CalculoDTO(String nome, String especie, LocalDateTime data){
        this.nome = nome;
        this.data = data;
        this.especie = especie;
        
        
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

    public String getEspecie(){
        return especie;
    }

    public void setEspecie(String especie){
        this.especie = especie;
    }

}
