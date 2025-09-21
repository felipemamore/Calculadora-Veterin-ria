package com.calculadora.veterinaria.backend.repository;

import com.calculadora.veterinaria.backend.entity.Alimento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AlimentoRepository extends JpaRepository<Alimento, Long> {
    Optional<Alimento> findByNomeIgnoreCase(String nome);
}