package com.calculadora.veterinaria.backend.service;

import com.calculadora.veterinaria.backend.dto.DoseRequest;
import com.calculadora.veterinaria.backend.dto.DoseResponse;
import com.calculadora.veterinaria.backend.entity.Calculo;
import com.calculadora.veterinaria.backend.entity.Dosagem;
import com.calculadora.veterinaria.backend.entity.DosagemSilvestre; // Importe a nova entidade
import com.calculadora.veterinaria.backend.entity.Especie;
import com.calculadora.veterinaria.backend.entity.Medicamento;
import com.calculadora.veterinaria.backend.entity.Usuario;
import com.calculadora.veterinaria.backend.repository.CalculoRepository;
import com.calculadora.veterinaria.backend.repository.DosagemRepository;
import com.calculadora.veterinaria.backend.repository.DosagemSilvestreRepository; // Importe o novo repositório
import com.calculadora.veterinaria.backend.repository.EspecieRepository;
import com.calculadora.veterinaria.backend.repository.MedicamentoRepository;
import com.calculadora.veterinaria.backend.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private DosagemSilvestreRepository dosagemSilvestreRepository;

    private final Map<String, Double> constantesK;

    public CalculoService() {
        constantesK = new HashMap<>();
        // Definindo os valores de K (Constante Metabólica)
        constantesK.put("Passeriformes", 129.0);
        constantesK.put("Não-Passeriformes", 78.0);
        constantesK.put("Aves", 78.0); 
        constantesK.put("Mamíferos Placentários", 70.0);
        constantesK.put("Mamíferos", 70.0);
        constantesK.put("Marsupiais", 49.0);
        constantesK.put("Répteis", 10.0); 
        constantesK.put("Anfíbios", 10.0); 
    }

    public DoseResponse calcularDose(DoseRequest request) {
        Medicamento medicamento = medicamentoRepository.findById(request.getMedicamentoId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicamento não encontrado"));

        Especie especie = especieRepository.findById(request.getEspecieId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Espécie não encontrada"));

        List<Dosagem> dosagens = dosagemRepository.findByMedicamentoIdAndEspecieId(medicamento.getId(), especie.getId());
        if (dosagens.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dosagem não encontrada para este medicamento e espécie");
        }
        Dosagem dosagem = dosagens.get(0); 

        Usuario usuario = getUsuarioLogado();

        Calculo calculo = new Calculo();
        calculo.setDosagem(dosagem);
        calculo.setPesoKg(request.getPesoKg());
        calculo.setDataHora(LocalDateTime.now());
        calculo.setUsuario(usuario);
        calculoRepository.save(calculo);
        double dose = (request.getPesoKg() * dosagem.getDoseRecomendadaMgPorKg()) / dosagem.getConcentracaoMgPorMl();

        return new DoseResponse(dose);
    }

    
    public Double calcularDoseSilvestre(String grupo, String medicamento, Double pesoGramas) {
        DosagemSilvestre dados = dosagemSilvestreRepository.findByGrupoAndMedicamento(grupo, medicamento);
        
        if (dados == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dosagem não encontrada para este grupo e medicamento");
        }
        Double pesoKg = pesoGramas / 1000.0;
        
        Double k = constantesK.getOrDefault(grupo, 70.0); 
        
        Double tmb = k * Math.pow(pesoKg, 0.75);

        Double doseTotalMg = dados.getDoseMgKg() * (tmb / 10.0);

        return doseTotalMg;
    }

    private Usuario getUsuarioLogado() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && ! (authentication.getPrincipal() instanceof String && "anonymousUser".equals(authentication.getPrincipal()))) {
            String username = authentication.getName();
            return usuarioRepository.findByEmail(username).orElse(null);
        }
        return null;
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