package com.calculadora.veterinaria.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.calculadora.veterinaria.backend.entity.Calculo;
import com.calculadora.veterinaria.backend.entity.Usuario;

public interface CalculoRepository extends JpaRepository<Calculo, Long> {
    List<Calculo> findTop5ByUsuarioOrderByDataHoraDesc(Usuario usuario);

    @Query("SELECT c FROM Calculo c WHERE c.usuario = :usuario AND c.dosagem.especie.nome = :nomeEspecie ORDER BY c.dataHora DESC LIMIT 5")
    List<Calculo> findTop5ByUsuarioAndEspecie(Usuario usuario, String nomeEspecie);
}

