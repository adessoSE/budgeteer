package de.adesso.budgeteer.rest.security.cors;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(value = CorsProperties.class)
public class CorsConfiguration {

    private final CorsProperties corsProperties;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowedOrigins(corsProperties.getAllowedOrigins());
        configuration.setAllowedMethods(corsProperties.getAllowedMethods());
        configuration.setAllowedHeaders(corsProperties.getAllowedHeaders());
        configuration.setAllowCredentials(corsProperties.isAllowedCredentials());
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
