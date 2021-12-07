package de.adesso.budgeteer.core.project.port.in;

import de.adesso.budgeteer.core.project.domain.Project;
import de.adesso.budgeteer.core.project.ProjectException;
import lombok.Value;

public interface CreateProjectUseCase {
    Project createProject(CreateProjectCommand command) throws ProjectException;

    @Value
    class CreateProjectCommand {
        String name;
        long userId;
    }
}
