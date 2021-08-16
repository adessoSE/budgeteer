package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.ProjectNameAlreadyInUseException;
import de.adesso.budgeteer.core.project.domain.Project;
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
    public Project createProject(CreateProjectCommand command) throws ProjectNameAlreadyInUseException{
        if (projectExistsWithNamePort.projectExistsWithName(command.getName())) {
            throw new ProjectNameAlreadyInUseException();
        }
        return createProjectPort.createProject(command.getUserId(), command.getName());
    }
}
