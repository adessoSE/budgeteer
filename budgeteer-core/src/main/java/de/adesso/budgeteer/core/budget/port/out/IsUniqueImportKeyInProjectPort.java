package de.adesso.budgeteer.core.budget.port.out;

public interface IsUniqueImportKeyInProjectPort {
    boolean isUniqueImportKeyInProject(long projectId, String importKey);
}
