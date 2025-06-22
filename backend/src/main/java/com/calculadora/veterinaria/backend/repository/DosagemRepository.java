package com.calculadora.veterinaria.backend.repository;

import com.calculadora.veterinaria.backend.entity.Dosagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DosagemRepository extends JpaRepository<Dosagem, Long> {
    Optional<Dosagem> findByMedicamentoIdAndEspecieId(Long medicamentoId, Long especieId);
}
