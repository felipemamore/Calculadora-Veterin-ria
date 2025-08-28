package com.calculadora.veterinaria.backend.service;

import com.calculadora.veterinaria.backend.dto.DoseRequest;
import com.calculadora.veterinaria.backend.dto.DoseResponse;
import com.calculadora.veterinaria.backend.entity.Calculo;
import com.calculadora.veterinaria.backend.entity.Dosagem;
import com.calculadora.veterinaria.backend.entity.Especie;
import com.calculadora.veterinaria.backend.entity.Medicamento;
import com.calculadora.veterinaria.backend.entity.Usuario;
import com.calculadora.veterinaria.backend.repository.CalculoRepository;
import com.calculadora.veterinaria.backend.repository.DosagemRepository;
import com.calculadora.veterinaria.backend.repository.EspecieRepository;
import com.calculadora.veterinaria.backend.repository.MedicamentoRepository;
import com.calculadora.veterinaria.backend.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class CalculoService {

    @Autowired
    private DosagemRepository dosagemRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private EspecieRepository especieRepository;

    @Autowired
    private CalculoRepository calculoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public DoseResponse calcularDose(DoseRequest request) {

        Medicamento medicamento = medicamentoRepository.findById(request.getMedicamentoId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicamento não encontrado"));

        Especie especie = especieRepository.findById(request.getEspecieId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Espécie não encontrada"));

        Dosagem dosagem = dosagemRepository.findByMedicamentoIdAndEspecieId(medicamento.getId(), especie.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dosagem não encontrada para este medicamento e espécie"));

        Usuario usuario = null;
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && ! (authentication.getPrincipal() instanceof String && "anonymousUser".equals(authentication.getPrincipal()))) {
        String username = authentication.getName();
        usuario = usuarioRepository.findByEmail(username)
                .orElse(null);
        }

        Calculo calculo = new Calculo();
        calculo.setDosagem(dosagem);
        calculo.setPesoKg(request.getPesoKg());
        calculo.setDataHora(LocalDateTime.now());
        calculo.setUsuario(usuario);

        calculoRepository.save(calculo);

        double dose = (request.getPesoKg() * dosagem.getDoseRecomendadaMgPorKg()) / dosagem.getConcentracaoMgPorMl();

        return new DoseResponse(dose);
    }

    public List<Calculo> buscarUltimosCalculosDoUsuario() {
        Usuario usuario = null;
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            var principal = authentication.getPrincipal();
            if (principal instanceof Usuario) {
                usuario = (Usuario) principal;
            }
        }
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }

        return calculoRepository.findTop5ByUsuarioOrderByDataHoraDesc(usuario);
    }
}
