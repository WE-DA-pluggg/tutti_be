package com.tutti.backend.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedOrigins("http://localhost:3000/")
                .allowedOrigins("http://tuttimusic.shop/")
                .allowedOrigins("http://tuttimusic.shop")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowCredentials(true)
                .exposedHeaders("Authorization")
                .maxAge(3600);
    }
}
