package de.adesso.budgeteer.core.project.port.in;

public interface RemoveUserFromProjectPort {
    void removeUserFromProject(long userId, long projectId);
}
