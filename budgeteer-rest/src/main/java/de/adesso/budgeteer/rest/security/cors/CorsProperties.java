package de.adesso.budgeteer.rest.security.cors;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties("budgeteer.cors")
public class CorsProperties {
    private List<String> allowedOrigins;
    private List<String> allowedMethods;
    private List<String> allowedHeaders;
    private boolean allowedCredentials;
}
