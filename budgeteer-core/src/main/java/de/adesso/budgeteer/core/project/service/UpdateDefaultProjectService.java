package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.domain.Project;
import de.adesso.budgeteer.core.project.port.in.UpdateDefaultProjectUseCase;
import de.adesso.budgeteer.core.project.port.out.ProjectExistsWithIdPort;
import de.adesso.budgeteer.core.project.port.out.UpdateDefaultProjectPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateDefaultProjectService implements UpdateDefaultProjectUseCase {

    private final UpdateDefaultProjectPort updateDefaultProjectPort;
    private final ProjectExistsWithIdPort projectExistsWithIdPort;

    @Override
    public Optional<Project> updateDefaultProject(long userId, long projectId) {
        if (!projectExistsWithIdPort.projectExistsWithId(projectId)) {
            return Optional.empty();
        }
        return Optional.of(updateDefaultProjectPort.updateDefaultProject(userId, projectId));
    }
}
