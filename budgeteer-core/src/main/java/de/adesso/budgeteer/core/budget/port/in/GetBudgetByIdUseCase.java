package de.adesso.budgeteer.core.budget.port.in;

import de.adesso.budgeteer.core.budget.domain.Budget;

import java.util.Optional;

public interface GetBudgetByIdUseCase {
    Optional<Budget> getBudgetById(long budgetId);
}
