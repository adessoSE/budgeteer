package org.wickedsource.budgeteer;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.wickedsource.budgeteer.service.security.BudgeteerAuthenticationToken;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.web.BudgeteerApplication;
import org.wickedsource.budgeteer.web.BudgeteerSession;

/**
 * Test base class for service integration tests to set a authentication in the
 * authentication context before test runs.
 */
public class ServiceIntegrationTestTemplate {

    @Autowired
    BudgeteerApplication application;

    WicketTester tester;

    @BeforeEach
    public void setAuthentication() {
        // set a placeholder authentication
        SecurityContextHolder.getContext().setAuthentication(new BudgeteerAuthenticationToken("user"));

        if(tester == null)
        {
            WicketTester tester = new WicketTester(application);

            User user = new User();
            user.setId(1L);
            BudgeteerSession.get().login(user);
            BudgeteerSession.get().setProjectSelected(true);
        }
    }
}
