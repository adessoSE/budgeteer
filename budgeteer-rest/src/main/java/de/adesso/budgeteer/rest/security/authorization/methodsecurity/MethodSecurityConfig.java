package de.adesso.budgeteer.rest.security.authorization.methodsecurity;

import de.adesso.budgeteer.core.budget.port.in.UserHasAccessToBudgetUseCase;
import de.adesso.budgeteer.core.contract.port.in.UserHasAccessToContractUseCase;
import de.adesso.budgeteer.core.invoice.port.in.UserHasAccessToInvoiceUseCase;
import de.adesso.budgeteer.core.person.port.in.UserHasAccessToPersonUseCase;
import de.adesso.budgeteer.core.project.port.in.UserHasAccessToProjectUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    private final UserHasAccessToProjectUseCase userHasAccessToProjectUseCase;
    private final UserHasAccessToContractUseCase userHasAccessToContractUseCase;
    private final UserHasAccessToInvoiceUseCase userHasAccessToInvoiceUseCase;
    private final UserHasAccessToBudgetUseCase userHasAccessToBudgetUseCase;
    private final UserHasAccessToPersonUseCase userHasAccessToPersonUseCase;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new BudgeteerMethodSecurityExpressionHandler(userHasAccessToProjectUseCase, userHasAccessToContractUseCase, userHasAccessToInvoiceUseCase, userHasAccessToBudgetUseCase, userHasAccessToPersonUseCase);
    }

}
