package de.adesso.budgeteer.rest.security.jwt.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("budgeteer.jwt")
public class JwtProperties {
    private String secret;
    private String issuer;
    private long maxAge;
}
