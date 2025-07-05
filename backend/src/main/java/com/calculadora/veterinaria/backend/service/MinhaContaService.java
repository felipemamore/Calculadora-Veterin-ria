package com.calculadora.veterinaria.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calculadora.veterinaria.backend.dto.MinhaContaDTO;
import com.calculadora.veterinaria.backend.entity.Dosagem;
import com.calculadora.veterinaria.backend.entity.Usuario;
import com.calculadora.veterinaria.backend.repository.DosagemRepository;

@Service
public class MinhaContaService {

    @Autowired
    private DosagemRepository dosagemRepository;
    
    public MinhaContaDTO obterDadosMinhaConta(Usuario usuario) {

        List<Dosagem> todasDosagens = dosagemRepository.findAll(); // Sua implementação atual

        List<Dosagem> usadasPorUsuario = todasDosagens.stream()
            .filter(d -> d.getMedicamento() != null && d.getMedicamento().getNome() != null && d.getEspecie() != null && d.getEspecie().getNome() != null)
            .limit(10) // Mantenha ou ajuste o limite
            .collect(Collectors.toList());
            return new MinhaContaDTO(usuario, usadasPorUsuario);
    }
}
