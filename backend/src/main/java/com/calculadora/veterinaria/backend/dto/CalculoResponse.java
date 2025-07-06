package com.calculadora.veterinaria.backend.dto;

import java.time.LocalDateTime;

import com.calculadora.veterinaria.backend.entity.Calculo;

public class CalculoResponse {
    private String nomeMedicamento;
    private String nomeEspecie;
    private Double pesoKg;
    private LocalDateTime dataHora;

    public CalculoResponse(Calculo calculo) {
        this.nomeMedicamento = calculo.getDosagem().getMedicamento().getNome();
        this.nomeEspecie = calculo.getDosagem().getEspecie().getNome();
        this.pesoKg = calculo.getPesoKg();
        this.dataHora = calculo.getDataHora();
    }


    public String getNomeMedicamento() {
        return nomeMedicamento;
    }

    public void setNomeMedicamento(String nomeMedicamento) {
        this.nomeMedicamento = nomeMedicamento;
    }

    public String getNomeEspecie() {
        return nomeEspecie;
    }

    public void setNomeEspecie(String nomeEspecie) {
        this.nomeEspecie = nomeEspecie;
    }

    public Double getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(Double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}

