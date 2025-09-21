package com.calculadora.veterinaria.backend.controller;

import com.calculadora.veterinaria.backend.dto.AlimentoToxicoDTO; 
import com.calculadora.veterinaria.backend.entity.AlimentoToxicoEspecie;
import com.calculadora.veterinaria.backend.repository.AlimentoToxicoEspecieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AlimentoToxicoController {

    @Autowired
    private AlimentoToxicoEspecieRepository repository; 

    @GetMapping("/api/alimentos-toxicos")
    public ResponseEntity<List<AlimentoToxicoDTO>> getAlimentosToxicosPorEspecie(@RequestParam String especieNome) {
        List<AlimentoToxicoEspecie> relacoes = repository.findByEspecie_NomeIgnoreCase(especieNome);
        
        List<AlimentoToxicoDTO> dtos = relacoes.stream()
            .map(relacao -> new AlimentoToxicoDTO(
                relacao.getAlimento().getNome(), 
                relacao.getDescricao()         
            ))
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(dtos);
    }
}