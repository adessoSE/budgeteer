package de.adesso.budgeteer.core.budget.port.out;

public interface IsUniqueNameInProjectPort {
    boolean isUniqueNameInProject(long projectId, String name);
    boolean isUniqueNameInProjectByBudgetId(long budgetId, String name);
}
