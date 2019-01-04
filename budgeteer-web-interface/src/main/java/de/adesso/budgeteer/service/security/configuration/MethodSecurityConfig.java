package de.adesso.budgeteer.service.security.configuration;

import de.adesso.budgeteer.service.security.BudgeteerMethodSecurityExpressionHandler;
import de.adesso.budgeteer.service.security.BudgeteerMethodSecurityExpressionRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * A configuration to set up custom spring boot security expressions.
 *
 * @see BudgeteerMethodSecurityExpressionHandler
 * @see BudgeteerMethodSecurityExpressionRoot
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    private final BudgeteerMethodSecurityExpressionHandler handler;

    @Autowired
    public MethodSecurityConfig(BudgeteerMethodSecurityExpressionHandler handler) {
        this.handler = handler;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return handler;
    }

}
