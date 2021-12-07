package de.adesso.budgeteer.core.project.port.out;

import de.adesso.budgeteer.core.project.domain.ProjectWithDate;

public interface GetProjectWithDatePort {
    ProjectWithDate getProjectWithDate(long projectId);
}
