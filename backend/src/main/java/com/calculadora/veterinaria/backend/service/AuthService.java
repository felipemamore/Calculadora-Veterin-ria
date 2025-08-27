package com.calculadora.veterinaria.backend.service;
import com.calculadora.veterinaria.backend.entity.Usuario;
import com.calculadora.veterinaria.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService; // <- Adicionado

    // Injetamos o TokenService no construtor
    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService; // <- Adicionado
    }

    public String login(String email, String senha) throws Exception {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Usuário não encontrado"));

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new Exception("Credenciais inválidas");
        }

        // AGORA USAMOS O TOKENSERVICE PARA GERAR O TOKEN
        return tokenService.gerarToken(usuario.getEmail());
    }

    // O MÉTODO private generateToken() FOI REMOVIDO DAQUI
}
    
