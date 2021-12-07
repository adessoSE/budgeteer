package de.adesso.budgeteer.core.project.port.in;

import de.adesso.budgeteer.core.project.domain.Project;

public interface GetProjectUseCase {
    Project getProject(long projectId);
}
