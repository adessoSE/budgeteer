package de.adesso.budgeteer.core.project.port.out;

import de.adesso.budgeteer.common.date.DateRange;
import de.adesso.budgeteer.core.project.domain.Project;

public interface UpdateProjectPort {
    Project updateProject(long projectId, String name, DateRange dateRange);
}
