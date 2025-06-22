package com.calculadora.veterinaria.backend.service;

import com.calculadora.veterinaria.backend.dto.DoseRequest;
import com.calculadora.veterinaria.backend.dto.DoseResponse;
import com.calculadora.veterinaria.backend.entity.Dosagem;
import com.calculadora.veterinaria.backend.entity.Medicamento;
import com.calculadora.veterinaria.backend.repository.DosagemRepository;
import com.calculadora.veterinaria.backend.repository.EspecieRepository;
import com.calculadora.veterinaria.backend.repository.MedicamentoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class CalculoService {

    @Autowired
    private DosagemRepository dosagemRepository;

     @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private EspecieRepository especieRepository;

    public DoseResponse calcularDose(DoseRequest request) {

        Medicamento medicamento = medicamentoRepository.findById(request.getMedicamentoId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicamento não encontrado"));

        var especie = especieRepository.findById(request.getEspecieId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Espécie não encontrada"));

        Dosagem dosagem = dosagemRepository.findByMedicamentoIdAndEspecieId(medicamento.getId(), especie.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dosagem não encontrada para este medicamento e espécie"));

        double dose = (request.getPesoKg() * dosagem.getDoseRecomendadaMgPorKg()) / dosagem.getConcentracaoMgPorMl();

        return new DoseResponse(dose);
    }
}
