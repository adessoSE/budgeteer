package org.wickedsource.budgeteer.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

// excluding configurations since they are not compatible with libraries within the application
@EnableAutoConfiguration(exclude = {JacksonAutoConfiguration.class})
@ComponentScan("org.wickedsource.budgeteer")
public class BudgeteerBooter {

    public static void main(String[] args) {
        SpringApplication.run(BudgeteerBooter.class, args);
    }

}
