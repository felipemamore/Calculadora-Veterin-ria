package com.calculadora.veterinaria.backend.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.calculadora.veterinaria.backend.entity.Dosagem;
import com.calculadora.veterinaria.backend.entity.Usuario;

public class MinhaContaDTO {
    private String nome;
    private String email;
    private List<String> historicoMedicamentos;

    public MinhaContaDTO(Usuario usuario, List<Dosagem> dosagens) {
        this.nome = usuario.getNomeCompleto();
        this.email = usuario.getEmail();
        this.historicoMedicamentos = dosagens.stream()
            .filter(d -> d.getMedicamento() != null && d.getMedicamento().getNome() != null &&
                         d.getEspecie() != null && d.getEspecie().getNome() != null)
            .map(d -> d.getMedicamento().getNome() + " (" + d.getEspecie().getNome() + ")")
            .distinct()
            .limit(5)
            .collect(Collectors.toList());
    }

    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public List<String> getHistoricoMedicamentos() { return historicoMedicamentos; }
}

