package org.wickedsource.budgeteer;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
Required to be able to run Tests with {@link SpringBootApplication}
 */
@SpringBootApplication(scanBasePackages = "org.wickedsource.budgeteer")
//@EnableJpaRepositories("de.adesso.budgeteer.persistence")
//@EntityScan("de.adesso.budgeteer.persistence")
public class TestApplication {
}
