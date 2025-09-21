package com.calculadora.veterinaria.backend.repository;

import com.calculadora.veterinaria.backend.entity.AlimentoToxicoEspecie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlimentoToxicoEspecieRepository extends JpaRepository<AlimentoToxicoEspecie, Long> {
    List<AlimentoToxicoEspecie> findByEspecie_NomeIgnoreCase(String nomeEspecie);
}