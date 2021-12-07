package de.adesso.budgeteer.rest.security.authorization.methodsecurity;

import de.adesso.budgeteer.core.budget.port.in.UserHasAccessToBudgetUseCase;
import de.adesso.budgeteer.core.contract.port.in.UserHasAccessToContractUseCase;
import de.adesso.budgeteer.core.invoice.port.in.UserHasAccessToInvoiceUseCase;
import de.adesso.budgeteer.core.person.port.in.UserHasAccessToPersonUseCase;
import de.adesso.budgeteer.core.project.port.in.UserHasAccessToProjectUseCase;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class BudgeteerMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private final UserHasAccessToProjectUseCase userHasAccessToProjectUseCase;
    private final UserHasAccessToContractUseCase userHasAccessToContractUseCase;
    private final UserHasAccessToInvoiceUseCase userHasAccessToInvoiceUseCase;
    private final UserHasAccessToBudgetUseCase userHasAccessToBudgetUseCase;
    private final UserHasAccessToPersonUseCase userHasAccessToPersonUseCase;

    public BudgeteerMethodSecurityExpressionRoot(Authentication authentication, UserHasAccessToProjectUseCase userHasAccessToProjectUseCase, UserHasAccessToContractUseCase userHasAccessToContractUseCase, UserHasAccessToInvoiceUseCase userHasAccessToInvoiceUseCase, UserHasAccessToBudgetUseCase userHasAccessToBudgetUseCase, UserHasAccessToPersonUseCase userHasAccessToPersonUseCase) {
        super(authentication);
        this.userHasAccessToProjectUseCase = userHasAccessToProjectUseCase;
        this.userHasAccessToContractUseCase = userHasAccessToContractUseCase;
        this.userHasAccessToInvoiceUseCase = userHasAccessToInvoiceUseCase;
        this.userHasAccessToBudgetUseCase = userHasAccessToBudgetUseCase;
        this.userHasAccessToPersonUseCase = userHasAccessToPersonUseCase;
    }

    public boolean userHasAccessToProject(long projectId) {
        return userHasAccessToProjectUseCase.userHasAccessToProject(authentication.getName(), projectId);
    }

    public boolean userHasAccesToContract(long contractId) {
        return userHasAccessToContractUseCase.userHasAccessToContract(authentication.getName(), contractId);
    }

    public boolean userHasAccessToInvoice(long invoiceId) {
        return userHasAccessToInvoiceUseCase.userHasAccessToInvoice(authentication.getName(), invoiceId);
    }

    public boolean userHasAccessToBudget(long budgetId) {
        return userHasAccessToBudgetUseCase.userHasAccessToBudget(authentication.getName(), budgetId);
    }

    public boolean userHasAccessToPerson(long personId) {
        return userHasAccessToPersonUseCase.userHasAccessToPerson(authentication.getName(), personId);
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
