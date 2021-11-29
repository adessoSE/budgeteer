package de.adesso.budgeteer.rest.security.authorization.methodsecurity;

import de.adesso.budgeteer.core.project.port.in.UserHasAccessToProjectUseCase;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.DenyAllPermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
public class BudgeteerMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private final UserHasAccessToProjectUseCase userHasAccessToProjectUseCase;

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        var root = new BudgeteerMethodSecurityExpressionRoot(authentication, userHasAccessToProjectUseCase);
        root.setPermissionEvaluator(new DenyAllPermissionEvaluator());
        root.setTrustResolver(new AuthenticationTrustResolverImpl());
        return root;
    }

}
