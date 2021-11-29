package de.adesso.budgeteer.rest.security.authorization.methodsecurity;

import de.adesso.budgeteer.core.project.port.in.GetProjectUseCase;
import de.adesso.budgeteer.core.project.port.in.UserHasAccessToProjectUseCase;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class BudgeteerMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private final UserHasAccessToProjectUseCase userHasAccessToProjectUseCase;

    public BudgeteerMethodSecurityExpressionRoot(Authentication authentication, UserHasAccessToProjectUseCase userHasAccessToProjectUseCase) {
        super(authentication);
        this.userHasAccessToProjectUseCase = userHasAccessToProjectUseCase;
    }

    public boolean userHasAccessToProject(String username, long projectId) {
        return userHasAccessToProjectUseCase.userHasAccessToProject(username, projectId);
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public void setFilterObject(Object obj) {
        this.filterObject = obj;
    }

    @Override
    public void setReturnObject(Object obj) {
        this.returnObject = obj;
    }

    @Override
    public Object getThis() {
        return this;
    }
}
