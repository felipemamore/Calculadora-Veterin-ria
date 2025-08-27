package com.calculadora.veterinaria.backend.security;

import com.calculadora.veterinaria.backend.service.TokenService;
import com.calculadora.veterinaria.backend.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(TokenService tokenService, CustomUserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
        System.out.println("DEBUG: JwtAuthenticationFilter está sendo criado.");
    }

   @Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain)
        throws ServletException, IOException {

    // 1. Pega o cabeçalho de autorização da requisição.
    final String authHeader = request.getHeader("Authorization");

    // 2. Verifica se o cabeçalho existe e se começa com "Bearer ".
    // Se não existir, a requisição pode ser para um endpoint público.
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        // Deixa a requisição continuar seu caminho.
        // Se a rota for protegida, o Spring Security a bloqueará mais tarde.
        // Se for pública, ela será permitida.
        filterChain.doFilter(request, response);
        return; // Encerra a execução do filtro para esta requisição.
    }

    // 3. Se o cabeçalho existir, extrai o token.
    String token = authHeader.substring(7);

    // 4. Valida o token e, se for válido, define a autenticação no contexto de segurança.
    // Este bloco só é executado se a requisição de fato veio com um token.
    try {
        String email = tokenService.getEmailFromToken(token);
        // Garante que o usuário ainda não está autenticado nesta sessão.
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    } catch (Exception e) {
        // Se houver um erro na validação do token (expirado, inválido, etc.),
        // apenas registramos o erro e deixamos a requisição passar sem autenticação.
        // O Spring Security irá barrá-la mais tarde se a rota for protegida.
        System.err.println("Erro ao processar o token JWT: " + e.getMessage());
    }

    // 5. Passa a requisição (agora possivelmente autenticada) para o próximo filtro na cadeia.
    filterChain.doFilter(request, response);
    }
}