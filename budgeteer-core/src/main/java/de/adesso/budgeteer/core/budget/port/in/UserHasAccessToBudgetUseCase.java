package de.adesso.budgeteer.core.budget.port.in;

public interface UserHasAccessToBudgetUseCase {
    boolean userHasAccessToBudget(String username, long budgetId);
}
