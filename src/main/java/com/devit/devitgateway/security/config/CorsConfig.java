package com.devit.devitgateway.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {
    private static final String FRONTEND_LOCALHOST = "http://localhost:8080";
    private static final String FRONTEND_STAGING1 = "https://www.devit.shop";
    private static final String FRONTEND_STAGING2 = "https://devit.shop";

    /**
     * cors 설정
     */
    @Bean
    CorsConfigurationSource corsConfiguration() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.applyPermitDefaultValues();
        corsConfig.addAllowedMethod(HttpMethod.PUT);
        corsConfig.addAllowedMethod(HttpMethod.DELETE);
        corsConfig.addAllowedMethod(HttpMethod.GET);
        corsConfig.addAllowedMethod(HttpMethod.HEAD);
        corsConfig.addAllowedMethod(HttpMethod.OPTIONS);
        corsConfig.addAllowedMethod(HttpMethod.POST);

        corsConfig.addAllowedHeader("*");
        corsConfig.setAllowCredentials(true);
        corsConfig.setAllowedOrigins(Arrays.asList(FRONTEND_LOCALHOST, FRONTEND_STAGING1, FRONTEND_STAGING2));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}

