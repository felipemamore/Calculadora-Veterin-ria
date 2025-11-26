package com.calculadora.veterinaria.backend.repository;

import com.calculadora.veterinaria.backend.entity.DosagemSilvestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DosagemSilvestreRepository extends JpaRepository<DosagemSilvestre, Long> {
    
    @Query("SELECT DISTINCT d.grupo FROM DosagemSilvestre d")
    List<String> findDistinctGrupos();

    List<DosagemSilvestre> findByGrupo(String grupo);
    
    DosagemSilvestre findByGrupoAndMedicamento(String grupo, String medicamento);
}