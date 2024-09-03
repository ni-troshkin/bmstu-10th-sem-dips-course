package com.gatewayservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("PUT", "DELETE", "GET", "POST", "PATCH", "OPTIONS")
            .allowedHeaders("Authorization", "Access-Control-Request-Method",
                            "Access-Control-Request-Headers", "Accept", "Origin", "DNT", "X-CustomHeader", "Keep-Alive", "User-Agent",
                            "X-Requested-With", "If-Modified-Since", "Cache-Control", "Content-Type", "Content-Range", "Range")
            .allowCredentials(false).maxAge(1728000);
    }
}
