package com.calculadora.veterinaria.backend.dto;

public class BulaDTO {
    private String nome;
    private String indicacoes;
    private String contraIndicacoes;
    private String efeitosAdversos;
    private String reproducao;
    private String superdosagem;
    private String farmacodinamica;
    private String farmacocinetica;
    private String monitoramento;

    // Getters e Setters 
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getIndicacoes() { return indicacoes; }
    public void setIndicacoes(String indicacoes) { this.indicacoes = indicacoes; }
    public String getContraIndicacoes() { return contraIndicacoes; }
    public void setContraIndicacoes(String contraIndicacoes) { this.contraIndicacoes = contraIndicacoes; }
    public String getEfeitosAdversos() { return efeitosAdversos; }
    public void setEfeitosAdversos(String efeitosAdversos) { this.efeitosAdversos = efeitosAdversos; }
    public String getReproducao() { return reproducao; }
    public void setReproducao(String reproducao) { this.reproducao = reproducao; }
    public String getSuperdosagem() { return superdosagem; }
    public void setSuperdosagem(String superdosagem) { this.superdosagem = superdosagem; }
    public String getFarmacodinamica() { return farmacodinamica; }
    public void setFarmacodinamica(String farmacodinamica) { this.farmacodinamica = farmacodinamica; }
    public String getFarmacocinetica() { return farmacocinetica; }
    public void setFarmacocinetica(String farmacocinetica) { this.farmacocinetica = farmacocinetica; }
    public String getMonitoramento() { return monitoramento; }
    public void setMonitoramento(String monitoramento) { this.monitoramento = monitoramento; }
}