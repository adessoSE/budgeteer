package de.adesso.budgeteer.core.project.port.in;

import de.adesso.budgeteer.common.date.DateRange;
import de.adesso.budgeteer.core.project.ProjectNameAlreadyInUseException;
import de.adesso.budgeteer.core.project.ProjectNotFoundException;
import de.adesso.budgeteer.core.project.domain.Project;
import lombok.Value;

public interface UpdateProjectUseCase {
    Project updateProject(UpdateProjectCommand command) throws ProjectNameAlreadyInUseException, ProjectNotFoundException;

    @Value
    class UpdateProjectCommand {
        long id;
        String name;
        DateRange dateRange;
    }
}
