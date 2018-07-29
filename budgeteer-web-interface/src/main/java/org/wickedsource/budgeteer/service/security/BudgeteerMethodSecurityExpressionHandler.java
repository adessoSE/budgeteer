package org.wickedsource.budgeteer.service.security;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

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
