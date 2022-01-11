package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.ProjectException;
import de.adesso.budgeteer.core.project.domain.Project;
import de.adesso.budgeteer.core.project.port.in.UpdateProjectUseCase;
import de.adesso.budgeteer.core.project.port.out.GetProjectPort;
import de.adesso.budgeteer.core.project.port.out.ProjectExistsWithNamePort;
import de.adesso.budgeteer.core.project.port.out.UpdateProjectPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProjectService implements UpdateProjectUseCase {

  private final UpdateProjectPort updateProjectPort;
  private final ProjectExistsWithNamePort projectExistsWithNamePort;
  private final GetProjectPort getProjectPort;

  @Override
  public Project updateProject(UpdateProjectCommand command) throws ProjectException {
    var projectException = new ProjectException();

    if (command.getName() == null || command.getName().isBlank()) {
      throw new IllegalArgumentException("name may not be null or blank");
    }
    if (command.getDateRange() == null) {
      throw new IllegalArgumentException("dateRange may not be null");
    }

    var project = getProjectPort.getProject(command.getId());
    if (project == null) {
      projectException.addCause(ProjectException.ProjectErrors.PROJECT_NOT_FOUND);
      throw projectException;
    }
    if (!command.getName().equals(project.getName())
        && projectExistsWithNamePort.projectExistsWithName(command.getName())) {
      projectException.addCause(ProjectException.ProjectErrors.NAME_ALREADY_IN_USE);
      throw projectException;
    }

    return updateProjectPort.updateProject(
        command.getId(), command.getName(), command.getDateRange());
  }
}
