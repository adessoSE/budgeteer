package de.adesso.budgeteer.rest.project;

import de.adesso.budgeteer.core.project.ProjectException;
import de.adesso.budgeteer.core.project.port.in.*;
import de.adesso.budgeteer.rest.project.exceptions.CreateProjectException;
import de.adesso.budgeteer.rest.project.exceptions.UpdateProjectException;
import de.adesso.budgeteer.rest.project.model.*;
import de.adesso.budgeteer.rest.security.userdetails.UserDetailsImpl;
import de.adesso.budgeteer.rest.user.model.UserIdModel;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
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
  @PreAuthorize("userHasAccessToProject(#projectIdModel.value)")
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
  @PreAuthorize("userHasAccessToProject(#projectIdModel.value)")
  public void addUserToProject(
      @PathVariable("projectId") ProjectIdModel projectIdModel,
      @RequestParam UserIdModel userIdModel) {
    addUserToProjectUseCase.addUserToProject(userIdModel.getValue(), projectIdModel.getValue());
  }

  @DeleteMapping("/{projectId}/user")
  @PreAuthorize("userHasAccessToProject(#projectIdModel.value)")
  public void removeUserFromProject(
      @PathVariable("projectId") ProjectIdModel projectIdModel,
      @RequestParam UserIdModel userIdModel) {
    removeUserFromProjectUseCase.removeUserFromProject(
        userIdModel.getValue(), projectIdModel.getValue());
  }

  @DeleteMapping("/{projectId}")
  @PreAuthorize("userHasAccessToProject(#projectIdModel.value)")
  public void deleteProject(@PathVariable("projectId") ProjectIdModel projectIdModel) {
    deleteProjectUseCase.deleteProject(projectIdModel.getValue());
  }

  @PutMapping("/{projectId}")
  @PreAuthorize("userHasAccessToProject(#projectIdModel.value)")
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
  @PreAuthorize("userHasAccessToProject(#projectIdModel.value)")
  public List<String> getProjectAttributes(
      @PathVariable("projectId") ProjectIdModel projectIdModel) {
    return getProjectAttributesUseCase.getProjectAttributes(projectIdModel.getValue());
  }

  @GetMapping("/{projectId}/withDate")
  @PreAuthorize("userHasAccessToProject(#projectIdModel.value)")
  public ProjectWithDateModel getProjectWithDate(
      @PathVariable("projectId") ProjectIdModel projectIdModel) {
    return projectModelMapper.toModel(
        getProjectWithDateUseCase.getProjectWithDate(projectIdModel.getValue()));
  }
}
