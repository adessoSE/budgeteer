package de.adesso.budgeteer.core.project.port.in;

public interface RemoveUserFromProjectUseCase {
    void removeUserFromProject(long userId, long projectId);
}
