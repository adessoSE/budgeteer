package de.adesso.budgeteer.rest.project;

import de.adesso.budgeteer.core.project.ProjectException;
import de.adesso.budgeteer.core.project.port.in.*;
import de.adesso.budgeteer.rest.project.errors.CreateProjectError;
import de.adesso.budgeteer.rest.project.errors.UpdateProjectError;
import de.adesso.budgeteer.rest.project.exceptions.CreateProjectException;
import de.adesso.budgeteer.rest.project.exceptions.UpdateProjectException;
import de.adesso.budgeteer.rest.project.model.*;
import de.adesso.budgeteer.rest.security.authorization.aspects.annotations.HasAccessToProject;
import de.adesso.budgeteer.rest.security.userdetails.UserDetailsImpl;
import de.adesso.budgeteer.rest.user.model.UserIdModel;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
@HasAccessToProject
public class ProjectController {

  private final GetProjectUseCase getProjectUseCase;
  private final CreateProjectUseCase createProjectUseCase;
  private final GetUsersProjectsUseCase getUsersProjectsUseCase;
  private final GetDefaultProjectUseCase getDefaultProjectUseCase;
  private final UpdateDefaultProjectUseCase updateDefaultProjectUseCase;
  private final AddUserToProjectUseCase addUserToProjectUseCase;
  private final RemoveUserFromProjectUseCase removeUserFromProjectUseCase;
  private final DeleteProjectUseCase deleteProjectUseCase;
  private final UpdateProjectUseCase updateProjectUseCase;
  private final GetProjectAttributesUseCase getProjectAttributesUseCase;
  private final GetProjectWithDateUseCase getProjectWithDateUseCase;
  private final ProjectModelMapper projectModelMapper;

  @PostMapping
  public ProjectModel createProject(
      @Valid @RequestBody CreateProjectModel createProjectModel, Authentication authentication) {
    var user = (UserDetailsImpl) authentication.getPrincipal();
    try {
      return projectModelMapper.toModel(
          createProjectUseCase.createProject(
              new CreateProjectUseCase.CreateProjectCommand(
                  createProjectModel.getName(), user.getId())));
    } catch (ProjectException e) {
      throw new CreateProjectException(e);
    }
  }

  @GetMapping("/{projectId}")
  public Optional<ProjectModel> getProject(
      @PathVariable("projectId") ProjectIdModel projectIdModel) {
    return Optional.ofNullable(getProjectUseCase.getProject(projectIdModel.getValue()))
        .map(projectModelMapper::toModel);
  }

  @GetMapping
  public List<ProjectModel> getUsersProjects(Authentication authentication) {
    var user = (UserDetailsImpl) authentication.getPrincipal();
    return getUsersProjectsUseCase.getUsersProjects(user.getId()).stream()
        .map(projectModelMapper::toModel)
        .collect(Collectors.toList());
  }

  @GetMapping("/default")
  public Optional<ProjectModel> getUsersDefaultProject(Authentication authentication) {
    var user = (UserDetailsImpl) authentication.getPrincipal();
    return getDefaultProjectUseCase
        .getDefaultProject(user.getId())
        .map(projectModelMapper::toModel);
  }

  @PostMapping("/default")
  public Optional<ProjectModel> updateDefaultProject(
      @Valid @RequestBody UpdateDefaultProjectModel updateDefaultProjectModel,
      Authentication authentication) {
    var user = (UserDetailsImpl) authentication.getPrincipal();
    return updateDefaultProjectUseCase
        .updateDefaultProject(user.getId(), updateDefaultProjectModel.getNewDefaultProjectId())
        .map(projectModelMapper::toModel);
  }

  @PostMapping("/{projectId}/user")
  public void addUserToProject(
      @PathVariable("projectId") ProjectIdModel projectIdModel,
      @RequestParam UserIdModel userIdModel) {
    addUserToProjectUseCase.addUserToProject(userIdModel.getValue(), projectIdModel.getValue());
  }

  @DeleteMapping("/{projectId}/user")
  public void removeUserFromProject(
      @PathVariable("projectId") ProjectIdModel projectIdModel,
      @RequestParam UserIdModel userIdModel) {
    removeUserFromProjectUseCase.removeUserFromProject(
        userIdModel.getValue(), projectIdModel.getValue());
  }

  @DeleteMapping("/{projectId}")
  public void deleteProject(@PathVariable("projectId") ProjectIdModel projectIdModel) {
    deleteProjectUseCase.deleteProject(projectIdModel.getValue());
  }

  @PutMapping("/{projectId}")
  public ProjectModel updateProject(
      @PathVariable("projectId") ProjectIdModel projectIdModel,
      @Valid @RequestBody UpdateProjectModel updateProjectModel) {
    try {
      return projectModelMapper.toModel(
          updateProjectUseCase.updateProject(
              new UpdateProjectUseCase.UpdateProjectCommand(
                  projectIdModel.getValue(),
                  updateProjectModel.getName(),
                  updateProjectModel.getDateRange())));
    } catch (ProjectException e) {
      throw new UpdateProjectException(e);
    }
  }

  @GetMapping("/{projectId}/attributes")
  public List<String> getProjectAttributes(@PathVariable("projectId") ProjectIdModel projectId) {
    return getProjectAttributesUseCase.getProjectAttributes(projectId.getValue());
  }

  @GetMapping("/{projectId}/withDate")
  public ProjectWithDateModel getProjectWithDate(
      @PathVariable("projectId") ProjectIdModel projectIdModel) {
    return projectModelMapper.toModel(
        getProjectWithDateUseCase.getProjectWithDate(projectIdModel.getValue()));
  }

  @ExceptionHandler(CreateProjectException.class)
  public ResponseEntity<CreateProjectError> handleCreateProjectException(
      CreateProjectException createProjectException) {
    var causes = createProjectException.getProjectException().getCauses();

    var createProjectError = new CreateProjectError();
    createProjectError.setNamealreadyInUse(
        causes.contains(ProjectException.ProjectErrors.NAME_ALREADY_IN_USE));

    return ResponseEntity.badRequest().body(createProjectError);
  }

  @ExceptionHandler(UpdateProjectException.class)
  public ResponseEntity<UpdateProjectError> handleUpdateProjectException(
      UpdateProjectException updateProjectException) {
    var causes = updateProjectException.getProjectException().getCauses();

    var updateProjectError = new UpdateProjectError();
    updateProjectError.setProjectNotFound(
        causes.contains(ProjectException.ProjectErrors.PROJECT_NOT_FOUND));
    updateProjectError.setNameAlreadyInUse(
        causes.contains(ProjectException.ProjectErrors.NAME_ALREADY_IN_USE));

    return ResponseEntity.badRequest().body(updateProjectError);
  }
}
