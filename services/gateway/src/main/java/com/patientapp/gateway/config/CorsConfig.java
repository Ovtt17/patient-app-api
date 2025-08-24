package com.patientapp.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
public class CorsConfig {

    @Value("${application.front-end.url}")
    private String frontendUrl;

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList(frontendUrl));
        config.setAllowedMethods(List.of(
                GET.name(),
                POST.name(),
                PUT.name(),
                PATCH.name(),
                DELETE.name(),
                OPTIONS.name()
        ));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(List.of(
                AUTHORIZATION,
                ACCEPT,
                CONTENT_TYPE,
                "X-XSRF-TOKEN"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}