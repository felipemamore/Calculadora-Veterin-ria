package com.calculadora.veterinaria.backend.config;

import com.calculadora.veterinaria.backend.service.CustomUserDetailsService;
import com.calculadora.veterinaria.backend.security.JwtAuthenticationFilter;
import com.calculadora.veterinaria.backend.service.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TokenService tokenService;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(TokenService tokenService, CustomUserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenService, userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Liberar acesso a arquivos estáticos (css, js, imagens, páginas estáticas)
                .requestMatchers("/css/**", "/js/**", "/images/**", "/pages/**").permitAll()

                // Liberar endpoints públicos
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/senha/forgot-password").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/senha/reset-password").permitAll()

                // Corrigir para aceitar qualquer caminho após (ex: /api/medicamentos e /api/medicamentos/qualquerCoisa)
                .requestMatchers(HttpMethod.GET, "/api/medicamentos/**").permitAll()
                .requestMatchers("/especie.html", "/api/especie/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/toxicas").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/calculo/historico").permitAll()
                .requestMatchers("/api/calculo/dose").permitAll()
                .requestMatchers("/api/dosagem").permitAll()

                // Outras páginas públicas
                .requestMatchers("/","/minhaConta", "/home", "/especie", "/cadastro", "/redefinirSenha", "/pages/**",
                        "/pagina-login", "/css/**", "/templates/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/resetSenha.html").permitAll()

                .anyRequest().authenticated()
            )
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) // 401 para não autenticado
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
