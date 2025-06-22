package com.calculadora.veterinaria.backend.repository;

import com.calculadora.veterinaria.backend.entity.Especie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EspecieRepository extends JpaRepository<Especie, Long> {
    boolean existsByNome(String nome);
    Optional<Especie> findByNomeIgnoreCase(String nome);
}