package de.adesso.budgeteer.core.project.port.out;

import de.adesso.budgeteer.core.project.domain.Project;

import java.util.Optional;

public interface UpdateDefaultProjectPort {
    Project updateDefaultProject(long userId, long projectId);
}
