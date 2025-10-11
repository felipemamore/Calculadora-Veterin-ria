package com.calculadora.veterinaria.backend.repository;

import com.calculadora.veterinaria.backend.entity.Dosagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DosagemRepository extends JpaRepository<Dosagem, Long> {
    List<Dosagem> findByMedicamentoIdAndEspecieId(Long medicamentoId, Long especieId);
    Optional<Dosagem> findByMedicamentoIdAndEspecieIdAndApresentacaoIgnoreCase(Long medicamentoId, Long especieId, String apresentacao);
}
