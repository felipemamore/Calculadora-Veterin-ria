package com.calculadora.veterinaria.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.calculadora.veterinaria.backend.entity.Especie;
import com.calculadora.veterinaria.backend.entity.MedicacaoToxica;
import com.calculadora.veterinaria.backend.entity.Medicamento;

public interface MedicacaoToxicaRepository extends JpaRepository<MedicacaoToxica, Long> {
    List<MedicacaoToxica> findByEspecieNomeIgnoreCase(String nomeEspecie);
    boolean existsByEspecieAndMedicamento(Especie especie, Medicamento medicamento);

}
