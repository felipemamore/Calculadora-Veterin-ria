package com.calculadora.veterinaria.backend.service;

import com.calculadora.veterinaria.backend.entity.PasswordResetToken;
import com.calculadora.veterinaria.backend.entity.Usuario;
import com.calculadora.veterinaria.backend.repository.PasswordResetTokenRepository;
import com.calculadora.veterinaria.backend.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecuperacaoSenhaService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public RecuperacaoSenhaService(UsuarioRepository usuarioRepository, 
    PasswordResetTokenRepository tokenRepository, 
    PasswordEncoder passwordEncoder, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public Optional<String> gerarTokenParaEmail(String email) {
        return usuarioRepository.findByEmail(email).map(usuario -> {
            String token = UUID.randomUUID().toString();
            LocalDateTime expiracao = LocalDateTime.now().plusMinutes(30);

            PasswordResetToken tokenObj = new PasswordResetToken(token, usuario, expiracao);
            tokenRepository.save(tokenObj);
            return token;
        });
    }

    @Transactional
    public boolean redefinirSenha(String token, String novaSenha) {
        return tokenRepository.findByToken(token)
            .filter(t -> t.getExpiracao().isAfter(LocalDateTime.now()))
            .map(t -> {
                Usuario usuario = t.getUsuario();

                String senhaCriptografada = passwordEncoder.encode(novaSenha);
                usuario.setSenha(senhaCriptografada);

                usuarioRepository.save(usuario);
                usuarioRepository.flush();

                System.out.println("Senha salva com sucesso no banco.");

                tokenRepository.delete(t);
                return true;
            }).orElse(false);
    }


    @Value("${APP_BASE_URL}")
    private String appBaseUrl;

        public boolean solicitarRedefinicaoSenha(String email) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(email);

    if (optionalUsuario.isPresent()) {
        Usuario usuario = optionalUsuario.get();
        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken(token, usuario, LocalDateTime.now().plusHours(2));
        tokenRepository.save(resetToken);

        String link = appBaseUrl + "/pages/resetSenha.html?token=" + token;
        String mensagem = "Você solicitou a redefinição de senha. Clique no link abaixo para continuar:\n" + link;

        emailService.enviarEmail(usuario.getEmail(), "Recuperação de Senha", mensagem);

        return true;
    }

    return false;
    }
}
