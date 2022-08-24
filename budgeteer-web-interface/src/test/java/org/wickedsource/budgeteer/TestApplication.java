package org.wickedsource.budgeteer;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/** Required to be able to run Tests with {@link SpringBootApplication} */
@SpringBootApplication(scanBasePackages = {"org.wickedsource.budgeteer", "de.adesso.budgeteer"})
// @EnableJpaRepositories("de.adesso.budgeteer.persistence")
// @EntityScan("de.adesso.budgeteer.persistence")
public class TestApplication {
  @Bean
  public JavaMailSender javaMailSender() {
    return new JavaMailSenderImpl();
  }
}
