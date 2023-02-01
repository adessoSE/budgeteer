package de.adesso.budgeteer;

import de.adesso.budgeteer.rest.RestConfig;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.wickedsource.budgeteer.BudgeteerWicketConfig;

@SpringBootApplication(
    scanBasePackages = {"de.adesso.budgeteer.core", "de.adesso.budgeteer.persistence"})
public class BudgeteerApplication {
  public static void main(String[] args) {
    new SpringApplicationBuilder()
        .sources(BudgeteerApplication.class)
        .web(WebApplicationType.NONE)
        .child(BudgeteerWicketConfig.class)
        .web(WebApplicationType.SERVLET)
        .properties("server.port=8080")
        .sibling(RestConfig.class)
        .web(WebApplicationType.SERVLET)
        .properties("server.port=8081")
        .run(args);
  }
}
