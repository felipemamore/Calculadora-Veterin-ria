package com.calculadora.veterinaria.backend.service;

import com.calculadora.veterinaria.backend.dto.AccountDetailsDTO;
import com.calculadora.veterinaria.backend.dto.ProfileUpdateDTO;
import com.calculadora.veterinaria.backend.entity.Usuario;
import com.calculadora.veterinaria.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MinhaContaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public AccountDetailsDTO getLoggedUserDetails() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado na sessão"));

        return new AccountDetailsDTO(usuario);
    }

    // Método para atualizar o perfil do usuário logado
    @Transactional
    public void updateLoggedUserProfile(ProfileUpdateDTO profileUpdateDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado na sessão"));

        usuario.setCpf(profileUpdateDto.getCpf());
        usuario.setRg(profileUpdateDto.getRg());
        usuario.setCrmv(profileUpdateDto.getCrmv());
        usuario.setOcupacao(profileUpdateDto.getOcupacao());
        usuarioRepository.save(usuario);
    }
}