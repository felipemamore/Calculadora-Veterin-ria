package com.calculadora.veterinaria.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dosagem_silvestre")
public class DosagemSilvestre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medicamento;
    private String grupo;
    private Double doseMgKg;
    private String acao;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMedicamento() { return medicamento; }
    public void setMedicamento(String medicamento) { this.medicamento = medicamento; }
    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }
    public Double getDoseMgKg() { return doseMgKg; }
    public void setDoseMgKg(Double doseMgKg) { this.doseMgKg = doseMgKg; }
    public String getAcao() { return acao; }
    public void setAcao(String acao) { this.acao = acao; }
}