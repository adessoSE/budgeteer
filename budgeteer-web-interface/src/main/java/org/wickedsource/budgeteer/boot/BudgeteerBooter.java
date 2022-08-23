package org.wickedsource.budgeteer.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"org.wickedsource.budgeteer", "de.adesso.budgeteer"})
@EnableJpaRepositories("de.adesso.budgeteer.persistence")
@EntityScan("de.adesso.budgeteer.persistence")
public class BudgeteerBooter {

  public static void main(String[] args) {
    SpringApplication.run(BudgeteerBooter.class, args);
  }
}
