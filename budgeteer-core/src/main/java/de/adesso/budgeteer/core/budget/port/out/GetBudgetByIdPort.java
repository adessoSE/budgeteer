package de.adesso.budgeteer.core.budget.port.out;

import de.adesso.budgeteer.core.budget.domain.Budget;

import java.util.Optional;

public interface GetBudgetByIdPort {
    Optional<Budget> getBudgetById(long budgetId);
}
