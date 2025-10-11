package com.calculadora.veterinaria.backend.config;

import com.calculadora.veterinaria.backend.security.JwtAuthenticationFilter;
import com.calculadora.veterinaria.backend.service.CustomUserDetailsService;
import com.calculadora.veterinaria.backend.service.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenService, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    // Recursos estáticos e páginas
            "/css/**", "/js/**", "/images/**", "/pages/**", "/templates/**", "/favicon.ico",
            "/", "/home", "/minhaConta", "/especie", "/cadastro", "/redefinirSenha",
            "/pagina-login", "/resetSenha", "/especie", "/error", "bula.html", 

            // Endpoints públicos da API
            "/api/bula",
            "/api/auth/**",
            "/api/users",
            "/api/senha/**",
            "/api/medicamentos/**",
            "/api/especie/**",
            "/api/dosagem/**",
            "/api/toxicas",
            "/api/calculo/historico",
            "/api/calculo/dose",
            "/api/alimentos-toxicos",
            "/api/ai/**"
        ).permitAll()

                // Qualquer outra requisição que não está na lista acima, precisa de autenticação
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite que o próprio site faça chamadas a si mesmo
        configuration.setAllowedOrigins(Arrays.asList("https://calculadora-veterinaria-api.fly.dev"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}