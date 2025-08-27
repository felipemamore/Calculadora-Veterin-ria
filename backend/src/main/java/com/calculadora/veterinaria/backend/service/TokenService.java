package com.calculadora.veterinaria.backend.service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secretKey;
    

    public String gerarToken(String email) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            // Versão sem o .withIssuer() para garantir consistência
            return JWT.create()
                    .withSubject(email)
                    .withExpiresAt(gerarDataDeExpiracao())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String getEmailFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            // CORREÇÃO: Removido o .withIssuer() para corresponder ao token gerado
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            // Se a assinatura for inválida ou o token expirar, a exceção será capturada
            // e o método retornará null, como esperado pelo filtro.
            return null;
        }
    }

    private Instant gerarDataDeExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}