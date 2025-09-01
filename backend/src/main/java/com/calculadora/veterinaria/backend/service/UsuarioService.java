package com.calculadora.veterinaria.backend.service;
import com.calculadora.veterinaria.backend.dto.UserRegistrationDTO;

import com.calculadora.veterinaria.backend.entity.Usuario;
import com.calculadora.veterinaria.backend.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario salvar(UserRegistrationDTO registrationDTO) {
        if (usuarioRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new IllegalArgumentException("O e-mail informado já está cadastrado.");
        }
        if (registrationDTO.getSenha() == null || registrationDTO.getSenha().isBlank()) {
            throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNomeCompleto(registrationDTO.getNomeCompleto());
        novoUsuario.setEmail(registrationDTO.getEmail());
        novoUsuario.setSenha(passwordEncoder.encode(registrationDTO.getSenha()));
        return usuarioRepository.save(novoUsuario);
}


    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }
}

