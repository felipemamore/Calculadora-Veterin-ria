package com.calculadora.veterinaria.backend.repository;

import com.calculadora.veterinaria.backend.entity.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
    boolean existsByNome(String nome);
    Optional<Medicamento> findByNomeIgnoreCase(String nome);
}
