package de.adesso.budgeteer.core.project.port.in;

import de.adesso.budgeteer.core.project.domain.Project;

import java.util.Optional;

public interface GetDefaultProjectUseCase {
    Optional<Project> getDefaultProject(long userId);
}
