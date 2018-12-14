package de.adesso.budgeteer;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.core.context.SecurityContextHolder;
import de.adesso.budgeteer.service.security.BudgeteerAuthenticationToken;

/**
 * Test base class for service integration tests to set a authentication in the
 * authentication context before test runs.
 */
public class ServiceIntegrationTestTemplate {

    @BeforeEach
    public void setAuthentication() {
        // set a placeholder authentication
        SecurityContextHolder.getContext().setAuthentication(new BudgeteerAuthenticationToken("user"));
    }
}
