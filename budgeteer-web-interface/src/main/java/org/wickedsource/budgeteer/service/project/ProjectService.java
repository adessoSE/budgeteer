package org.wickedsource.budgeteer.service.project;

import de.adesso.budgeteer.core.project.ProjectException;
import de.adesso.budgeteer.core.project.port.in.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.service.DateUtil;
import org.wickedsource.budgeteer.web.pages.administration.Project;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

  private final ProjectBaseDataMapper mapper;

  private final CreateProjectUseCase createProjectUseCase;
  private final GetUsersProjectsUseCase getUsersProjectsUseCase;
  private final GetDefaultProjectUseCase getDefaultProjectUseCase;
  private final DeleteProjectUseCase deleteProjectUseCase;
  private final UpdateDefaultProjectUseCase updateDefaultProjectUseCase;
  private final GetProjectWithDateUseCase getProjectWithDateUseCase;
  private final UpdateProjectUseCase updateProjectUseCase;

  /**
   * Creates a new empty project with the given name.
   *
   * @param projectName name of the project.
   * @return the base data of the newly create project.
   */
  public ProjectBaseData createProject(String projectName, long initialUserId)
      throws ProjectNameAlreadyInUseException {
    try {
      return mapper.map(
          createProjectUseCase.createProject(
              new CreateProjectUseCase.CreateProjectCommand(projectName, initialUserId)));
    } catch (ProjectException e) {
      if (e.contains(ProjectException.ProjectErrors.NAME_ALREADY_IN_USE)) {
        throw new ProjectNameAlreadyInUseException();
      }
      throw new IllegalStateException(e);
    }
  }

  /**
   * Returns all projects the given user has access to.
   *
   * @param userId ID of the user
   * @return list of all projects the user has access to.
   */
  public List<ProjectBaseData> getProjectsForUser(long userId) {
    return getUsersProjectsUseCase.getUsersProjects(userId).stream()
        .map(mapper::map)
        .collect(Collectors.toList());
  }

  /**
   * Returns the default project for a given user
   *
   * @param userId ID of the user
   * @return the default project respectively null if no project is set as default
   */
  public ProjectBaseData getDefaultProjectForUser(long userId) {
    return getDefaultProjectUseCase.getDefaultProject(userId).map(mapper::map).orElse(null);
  }

  /**
   * Deletes the given project and all its data from the database.
   *
   * @param projectId ID of the project to delete.
   */
  @PreAuthorize("canReadProject(#projectId)")
  public void deleteProject(long projectId) {
    deleteProjectUseCase.deleteProject(projectId);
  }

  /**
   * Sets the given project as the default project for the given user
   *
   * @param userId ID of the user for that the default project should be set
   * @param projectId ID of the project that should become the default-project
   */
  public void setDefaultProject(long userId, long projectId) {
    updateDefaultProjectUseCase.updateDefaultProject(userId, projectId);
  }

  public Project findProjectById(long projectId) {
    var project = getProjectWithDateUseCase.getProjectWithDate(projectId);
    return new Project(
        project.getId(),
        DateUtil.toDate(project.getDateRange().getStartDate()),
        DateUtil.toDate(project.getDateRange().getEndDate()),
        project.getName());
  }

  public void save(Project project) {
    try {
      updateProjectUseCase.updateProject(
          new UpdateProjectUseCase.UpdateProjectCommand(
              project.getProjectId(),
              project.getName(),
              new de.adesso.budgeteer.common.date.DateRange(
                  DateUtil.toLocalDate(project.getDateRange().getStartDate()),
                  DateUtil.toLocalDate(project.getDateRange().getEndDate()))));
    } catch (ProjectException e) {
      /* Do nothing */
    }
  }
}
