package com.calculadora.veterinaria.backend.dto;

public class DoseResponse {
    private double doseMl;

    public DoseResponse(double doseMl) {
        this.doseMl = doseMl;
    }

    public double getDose() {
        return doseMl;
    }

    public void setDoseMl(double doseMl) {
        this.doseMl = doseMl;
    }
}
