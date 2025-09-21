package com.calculadora.veterinaria.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull; // <-- 1. Adicione esta importação
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) { // <-- 2. Adicione a anotação aqui
        registry.addMapping("/api/**")
            .allowedOrigins( "https://calculadora-vet-frontend.fly.dev") 
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}