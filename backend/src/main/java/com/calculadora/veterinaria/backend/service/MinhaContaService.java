package com.calculadora.veterinaria.backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calculadora.veterinaria.backend.dto.CalculoResponse;
import com.calculadora.veterinaria.backend.dto.MinhaContaDTO;
import com.calculadora.veterinaria.backend.entity.Calculo;
import com.calculadora.veterinaria.backend.entity.Usuario;
import com.calculadora.veterinaria.backend.repository.CalculoRepository;

@Service
public class MinhaContaService {

    @Autowired
    private CalculoRepository calculoRepository;

    
    public MinhaContaDTO obterDadosMinhaConta(Usuario usuario) {

        List<Calculo> historico = calculoRepository.findTop5ByUsuarioOrderByDataHoraDesc(usuario);
        List<CalculoResponse> resposta = historico.stream().map(CalculoResponse::new).toList();
            return new MinhaContaDTO(usuario, resposta);
    }
}
