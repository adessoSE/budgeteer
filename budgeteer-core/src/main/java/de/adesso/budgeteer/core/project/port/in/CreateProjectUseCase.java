package de.adesso.budgeteer.core.project.port.in;

import de.adesso.budgeteer.core.project.ProjectNameAlreadyInUseException;
import de.adesso.budgeteer.core.project.domain.Project;
import lombok.Value;

public interface CreateProjectUseCase {
    Project createProject(CreateProjectCommand command) throws ProjectNameAlreadyInUseException;

    @Value
    class CreateProjectCommand {
        String name;
        long userId;
    }
}
