package de.adesso.budgeteer.core.project.port.out;

import de.adesso.budgeteer.core.project.domain.Project;

public interface CreateProjectPort {
    Project createProject(long userId, String name);
}
