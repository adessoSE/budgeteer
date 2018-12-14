package de.adesso.budgeteer.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// excluding configurations since they are not compatible with libraries within the application
@EnableAutoConfiguration(exclude = {JacksonAutoConfiguration.class, MultipartAutoConfiguration.class})
@EnableJpaRepositories("de.adesso.budgeteer.persistence")
@EntityScan("de.adesso.budgeteer.persistence")
@ComponentScan("de.adesso.budgeteer")
public class BudgeteerBooter {

    public static void main(String[] args) {
        SpringApplication.run(BudgeteerBooter.class, args);
    }

}
