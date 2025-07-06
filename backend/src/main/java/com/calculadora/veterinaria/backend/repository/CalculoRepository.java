package com.calculadora.veterinaria.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.calculadora.veterinaria.backend.entity.Calculo;
import com.calculadora.veterinaria.backend.entity.Usuario;

public interface CalculoRepository extends JpaRepository<Calculo, Long> {
    List<Calculo> findTop5ByUsuarioOrderByDataHoraDesc(Usuario usuario);
}

