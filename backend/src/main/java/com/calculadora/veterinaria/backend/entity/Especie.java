package com.calculadora.veterinaria.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Especie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @OneToMany(mappedBy = "especie")
    @JsonIgnore
    private List<Raca> racas;

    @OneToMany(mappedBy = "especie")
    @JsonManagedReference
    private List<Dosagem> dosagens;

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

    public List<Raca> getRacas() {
        return racas;
    }

    public void setRacas(List<Raca> racas) {
        this.racas = racas;
    }

    public List<Dosagem> getDosagens() {
        return dosagens;
    }

    public void setDosagens(List<Dosagem> dosagens) {
        this.dosagens = dosagens;
    }
}
