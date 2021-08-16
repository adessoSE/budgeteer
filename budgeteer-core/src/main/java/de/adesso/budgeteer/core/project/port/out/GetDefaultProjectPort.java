package de.adesso.budgeteer.core.project.port.out;

import de.adesso.budgeteer.core.project.domain.Project;

import java.util.Optional;

public interface GetDefaultProjectPort {
    Optional<Project> getDefaultProject(long userId);
}
