package de.adesso.budgeteer.core.project.port.out;

import de.adesso.budgeteer.core.project.domain.Project;

import java.util.List;

public interface GetUsersProjectsPort {
    List<Project> getUsersProjects(long userId);
}
