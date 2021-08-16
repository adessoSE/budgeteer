package de.adesso.budgeteer.core.project.port.in;

import de.adesso.budgeteer.core.project.domain.ProjectWithDate;

public interface GetProjectWithDateUseCase {
    ProjectWithDate getProjectWithDate(long projectId);
}
