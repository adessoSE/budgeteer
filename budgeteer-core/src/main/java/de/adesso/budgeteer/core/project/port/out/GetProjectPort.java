package de.adesso.budgeteer.core.project.port.out;

import de.adesso.budgeteer.core.project.domain.Project;

public interface GetProjectPort {
    Project getProject(long projectId);
}
