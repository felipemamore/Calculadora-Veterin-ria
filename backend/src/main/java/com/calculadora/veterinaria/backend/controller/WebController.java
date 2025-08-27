package com.calculadora.veterinaria.backend.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

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
    public String especie() {
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
