package com.calculadora.veterinaria.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Dosagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medicamento_id", nullable = false)
    @JsonBackReference
    private Medicamento medicamento;

    @ManyToOne
    @JoinColumn(name = "especie_id", nullable = false)
    @JsonBackReference
    private Especie especie;
    private Double doseRecomendadaMgPorKg;
    private Double concentracaoMgPorMl;

    @Column(nullable = false)
    private String apresentacao;

    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }


    public Double getDoseRecomendadaMgPorKg() {
        return doseRecomendadaMgPorKg;
    }

    public void setDoseRecomendadaMgPorKg(Double doseRecomendadaMgPorKg) {
        this.doseRecomendadaMgPorKg = doseRecomendadaMgPorKg;
    }

    public Double getConcentracaoMgPorMl() {
        return concentracaoMgPorMl;
    }

    public void setConcentracaoMgPorMl(Double concentracaoMgPorMl) {
        this.concentracaoMgPorMl = concentracaoMgPorMl;
    }
    public String getApresentacao() {
        return apresentacao;
    }

    public void setApresentacao(String apresentacao) {
        this.apresentacao = apresentacao;
    }
}
