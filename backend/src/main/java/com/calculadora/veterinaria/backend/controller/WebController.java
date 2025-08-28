package com.calculadora.veterinaria.backend.controller;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import com.calculadora.veterinaria.backend.entity.Calculo;
import com.calculadora.veterinaria.backend.entity.Especie;
import com.calculadora.veterinaria.backend.entity.MedicacaoToxica;
import com.calculadora.veterinaria.backend.entity.Usuario;
import com.calculadora.veterinaria.backend.repository.CalculoRepository;
import com.calculadora.veterinaria.backend.repository.EspecieRepository;
import com.calculadora.veterinaria.backend.repository.MedicacaoToxicaRepository;

@Controller
public class WebController {

    @Autowired
    private EspecieRepository especieRepository;
    @Autowired
    private CalculoRepository calculoRepository;
    @Autowired
    private MedicacaoToxicaRepository medicacaoToxicaRepository;

    @GetMapping("/")
    public String index() {
        return "index"; 
    }

    @GetMapping("/pagina-login")
    public String loginpage() {
        return "login"; 
    }

    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro"; 
    }

    @GetMapping("/home")
    public String home() {
        return "calculadora"; 
    }
    
    @GetMapping("/redefinirSenha")
    public String redefinirSenha() {
        return "redefinirSenha"; 
    }

    @GetMapping("/resetSenha")
    public String resetSenha() {
        return "resetSenha"; 
    }

    @GetMapping("/especie")
    public String especie(@RequestParam String nome, Model model, Authentication authentication) {
        Especie especie = especieRepository.findByNomeIgnoreCase(nome)
        .orElseThrow(() -> new RuntimeException("Espécie não encontrada: " + nome));

        List<MedicacaoToxica> toxicos = medicacaoToxicaRepository.findByEspecieNomeIgnoreCase(nome);

        List<Calculo> historicoCalculos = new ArrayList<>();

        if (authentication != null && authentication.isAuthenticated()) {
            Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
            historicoCalculos = calculoRepository.findTop5ByUsuarioAndEspecie(usuarioLogado, nome);
        }

        model.addAttribute("especie", especie);
        model.addAttribute("medicamentosToxicos", toxicos);
        model.addAttribute("historico", historicoCalculos);

        return "especie"; 
    }
    
    @GetMapping("/usuario")
    public String usuario(){
        return "usuario";
    }

    @GetMapping("/componenteCalculo")
    public String componenteCalculo() {
        return "componenteCalculo";
}
    @GetMapping("/minhaConta")
    public String minhaConta() {
        return "minhaConta"; 
    }


}
