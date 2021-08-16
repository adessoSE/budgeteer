package de.adesso.budgeteer.core.project.port.in;

import de.adesso.budgeteer.core.project.domain.Project;

import java.util.List;

public interface GetUsersProjectsUseCase {
    List<Project> getUsersProjects(long userId);
}
