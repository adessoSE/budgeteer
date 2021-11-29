package de.adesso.budgeteer.rest.security.authorization.methodsecurity;

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

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new BudgeteerMethodSecurityExpressionHandler(userHasAccessToProjectUseCase);
    }

}
