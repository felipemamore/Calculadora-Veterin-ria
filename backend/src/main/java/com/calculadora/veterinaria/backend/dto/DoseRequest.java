package com.calculadora.veterinaria.backend.dto;

public class DoseRequest {
    private Long medicamentoId;
    private double pesoKg;
    private Long especieId;

    // Getters e Setters
    public Long getMedicamentoId() {
        return medicamentoId;
    }

    public void setMedicamentoId(Long medicamentoId) {
        this.medicamentoId = medicamentoId;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public Long getEspecieId(){
        return especieId;
    }

    public void setEspecieID(Long especieId){
        this.especieId = especieId;

    }
}
