package com.mobisoft.taskmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedOrigins(
                            "http://localhost:5173",
                            "http://192.168.4.55:3000",
                            "http://localhost:3000",
                            "http://localhost:3001"
                        ) // Spécifie toutes les origines
                        .allowedHeaders("*")
                        .allowCredentials(true); // Si tu utilises des cookies ou l'authentification
            }
        };
    }
}

