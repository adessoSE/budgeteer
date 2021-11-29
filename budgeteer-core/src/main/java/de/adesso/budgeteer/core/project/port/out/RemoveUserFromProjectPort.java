package de.adesso.budgeteer.core.project.port.out;

public interface RemoveUserFromProjectPort {
    void removeUserFromProject(long userId, long projectId);
}
