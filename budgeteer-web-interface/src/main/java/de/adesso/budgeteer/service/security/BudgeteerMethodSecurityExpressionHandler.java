package de.adesso.budgeteer.service.security;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * {@link DefaultMethodSecurityExpressionHandler} to add methods which are specific to securing
 * method invocations (e.g. via {@link PreAuthorize})
 *
 * @see BudgeteerMethodSecurityExpressionRoot
 */
@Component
public class BudgeteerMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private final BudgeteerMethodSecurityExpressionRoot root;

    @Autowired
    public BudgeteerMethodSecurityExpressionHandler(BudgeteerMethodSecurityExpressionRoot root) {
        this.root = root;
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        return root;
    }

}
