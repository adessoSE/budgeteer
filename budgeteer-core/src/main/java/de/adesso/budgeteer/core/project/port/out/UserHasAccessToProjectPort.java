package de.adesso.budgeteer.core.project.port.out;

public interface UserHasAccessToProjectPort {
    boolean userHasAccessToProject(String username, long projectId);
}
