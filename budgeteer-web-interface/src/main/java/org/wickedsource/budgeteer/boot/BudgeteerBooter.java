package org.wickedsource.budgeteer.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// excluding configurations since they are not compatible with libraries within the application
@SpringBootApplication(scanBasePackages = "org.wickedsource.budgeteer")
@EnableJpaRepositories(basePackages = "org.wickedsource.budgeteer.persistence")
@EntityScan("org.wickedsource.budgeteer.persistence")
public class BudgeteerBooter {

    public static void main(String[] args) {
        SpringApplication.run(BudgeteerBooter.class, args);
    }

}
