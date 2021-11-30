package de.adesso.budgeteer.core.budget.port.out;

public interface UserHasAccessToBudgetPort {
    boolean userHasAccessToBudget(String username, long budgetId);
}
