package de.adesso.budgeteer.rest.security.authorization.methodsecurity;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.DenyAllPermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

public class BudgeteerMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        var root = new BudgeteerMethodSecurityExpressionRoot(authentication);
        root.setPermissionEvaluator(new DenyAllPermissionEvaluator());
        root.setTrustResolver(new AuthenticationTrustResolverImpl());
        return root;
    }

}
