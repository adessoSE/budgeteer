package de.adesso.budgeteer.core.budget.port.out;

import de.adesso.budgeteer.core.budget.domain.Budget;

import java.util.List;

public interface GetBudgetsInProjectPort {
    List<Budget> getBudgetsInProject(long projectId);
}
