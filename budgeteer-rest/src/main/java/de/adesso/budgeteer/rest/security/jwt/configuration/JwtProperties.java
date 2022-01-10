package de.adesso.budgeteer.rest.security.jwt.configuration;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties("budgeteer.jwt")
public class JwtProperties {

  @Length(min = 8)
  private String secret;

  @NotBlank private String issuer;

  private long maxAge;
}
