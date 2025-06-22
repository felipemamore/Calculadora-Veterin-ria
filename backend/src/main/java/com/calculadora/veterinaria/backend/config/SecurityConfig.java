package com.calculadora.veterinaria.backend.config;

import com.calculadora.veterinaria.backend.service.CustomUserDetailsService;
import com.calculadora.veterinaria.backend.security.JwtAuthenticationFilter;
import com.calculadora.veterinaria.backend.service.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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
            // liberar acesso aos arquivos estáticos (css, js, imagens, páginas estáticas)
            .requestMatchers("/css/**", "/js/**", "/images/**", "/pages/**").permitAll()
            
            // liberar endpoints públicos
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/senha/forgot-password").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/senha/reset-password").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/medicamentos").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/especies").permitAll()
            .requestMatchers("/api/calculo/dose").permitAll()
            .requestMatchers("/api/dosagem").permitAll()
            .requestMatchers("/","/home","/cadastro","/redefinirSenha","/pages/**",
                "/pagina-login", "/css/**","/templates/**", "/js/**", "/images/**").permitAll()

            // liberar acesso à página de resetSenha.html
            .requestMatchers("/resetSenha.html").permitAll()
            
            // o resto exige autenticação
            .anyRequest().authenticated()
        )
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

     
}
