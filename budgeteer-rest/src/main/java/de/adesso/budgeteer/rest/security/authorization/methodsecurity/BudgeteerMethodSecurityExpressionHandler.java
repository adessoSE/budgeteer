package de.adesso.budgeteer.rest.security.authorization.methodsecurity;

import de.adesso.budgeteer.core.budget.port.in.UserHasAccessToBudgetUseCase;
import de.adesso.budgeteer.core.contract.port.in.UserHasAccessToContractUseCase;
import de.adesso.budgeteer.core.invoice.port.in.UserHasAccessToInvoiceUseCase;
import de.adesso.budgeteer.core.person.port.in.UserHasAccessToPersonUseCase;
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
    private final UserHasAccessToContractUseCase userHasAccessToContractUseCase;
    private final UserHasAccessToInvoiceUseCase userHasAccessToInvoiceUseCase;
    private final UserHasAccessToBudgetUseCase userHasAccessToBudgetUseCase;
    private final UserHasAccessToPersonUseCase userHasAccessToPersonUseCase;

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        var root = new BudgeteerMethodSecurityExpressionRoot(authentication, userHasAccessToProjectUseCase, userHasAccessToContractUseCase, userHasAccessToInvoiceUseCase, userHasAccessToBudgetUseCase, userHasAccessToPersonUseCase);
        root.setPermissionEvaluator(new DenyAllPermissionEvaluator());
        root.setTrustResolver(new AuthenticationTrustResolverImpl());
        return root;
    }

}
