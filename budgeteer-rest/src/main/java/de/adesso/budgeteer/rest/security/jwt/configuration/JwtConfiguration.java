package de.adesso.budgeteer.rest.security.jwt.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = JwtProperties.class)
public class JwtConfiguration {
}
