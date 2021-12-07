package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.common.Causes;
import de.adesso.budgeteer.core.project.domain.Project;
import de.adesso.budgeteer.core.project.ProjectException;
import de.adesso.budgeteer.core.project.port.in.CreateProjectUseCase;
import de.adesso.budgeteer.core.project.port.out.CreateProjectPort;
import de.adesso.budgeteer.core.project.port.out.ProjectExistsWithNamePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateProjectService implements CreateProjectUseCase {

    private final CreateProjectPort createProjectPort;
    private final ProjectExistsWithNamePort projectExistsWithNamePort;

    @Override
    public Project createProject(CreateProjectCommand command) throws ProjectException {
        var causes = new Causes<ProjectException.ProjectErrors>();

        if (projectExistsWithNamePort.projectExistsWithName(command.getName())) {
            causes.addCause(ProjectException.ProjectErrors.NAME_ALREADY_IN_USE);
        }

        if (causes.hasCause()) {
            throw new ProjectException(causes);
        }

        return createProjectPort.createProject(command.getUserId(), command.getName());
    }
}
