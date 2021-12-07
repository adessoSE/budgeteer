package de.adesso.budgeteer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"de.adesso.budgeteer", "org.wickedsource.budgeteer.persistence"})
@EnableJpaRepositories(basePackages = "org.wickedsource.budgeteer.persistence")
@EntityScan(basePackages = "org.wickedsource.budgeteer.persistence")
public class BudgeteerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BudgeteerApplication.class, args);
    }
}
