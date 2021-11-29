package de.adesso.budgeteer.core.project.port.in;

public interface UserHasAccessToProjectUseCase {
    boolean userHasAccessToProject(String username, long projectId);
}
