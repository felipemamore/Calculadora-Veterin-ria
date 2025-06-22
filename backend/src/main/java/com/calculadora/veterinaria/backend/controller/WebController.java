package com.calculadora.veterinaria.backend.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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


}
