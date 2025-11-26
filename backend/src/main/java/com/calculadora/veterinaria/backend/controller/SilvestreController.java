package com.calculadora.veterinaria.backend.controller;

import com.calculadora.veterinaria.backend.entity.DosagemSilvestre;
import com.calculadora.veterinaria.backend.repository.DosagemSilvestreRepository;
import com.calculadora.veterinaria.backend.service.CalculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/silvestres")
public class SilvestreController {

    @Autowired
    private DosagemSilvestreRepository repository;

    @Autowired
    private CalculoService calculoService;

    @GetMapping("/grupos")
    public ResponseEntity<List<String>> listarGrupos() {
        return ResponseEntity.ok(repository.findDistinctGrupos());
    }

    @GetMapping("/medicamentos")
    public ResponseEntity<List<DosagemSilvestre>> listarPorGrupo(@RequestParam String grupo) {
        return ResponseEntity.ok(repository.findByGrupo(grupo));
    }

    @PostMapping("/calcular")
    public ResponseEntity<Map<String, Object>> calcular(@RequestBody Map<String, Object> payload) {
    String grupo = (String) payload.get("grupo");
    String medicamento = (String) payload.get("medicamento");
    
    Double pesoGramas = Double.valueOf(payload.get("peso").toString());

    Double resultadoMg = calculoService.calcularDoseSilvestre(grupo, medicamento, pesoGramas);

    DosagemSilvestre dados = repository.findByGrupoAndMedicamento(grupo, medicamento);

    return ResponseEntity.ok(Map.of(
        "medicamento", dados.getMedicamento(),
        "acao", dados.getAcao() != null ? dados.getAcao() : "",
        "doseTotalMg", resultadoMg
    ));
}
}