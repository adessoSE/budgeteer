package de.adesso.budgeteer.rest.project;

import de.adesso.budgeteer.core.project.ProjectNameAlreadyInUseException;
import de.adesso.budgeteer.core.project.ProjectNotFoundException;
import de.adesso.budgeteer.core.project.port.in.*;
import de.adesso.budgeteer.rest.project.model.*;
import de.adesso.budgeteer.rest.security.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ProjectModel createProject(@Valid @RequestBody CreateProjectModel createProjectModel, Authentication authentication) throws ProjectNameAlreadyInUseException {
        var user = (UserDetailsImpl) authentication.getPrincipal();
        return projectModelMapper.toModel(createProjectUseCase.createProject(new CreateProjectUseCase.CreateProjectCommand(createProjectModel.getName(), user.getId())));
    }

    @GetMapping("/{projectId}")
    @PreAuthorize("userHasAccessToProject(principal.username, #projectId)")
    public Optional<ProjectModel> getProject(@PathVariable("projectId") long projectId) {
        return Optional.ofNullable(getProjectUseCase.getProject(projectId)).map(projectModelMapper::toModel);
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
        return getDefaultProjectUseCase.getDefaultProject(user.getId()).map(projectModelMapper::toModel);
    }

    @PostMapping("/default")
    public Optional<ProjectModel> updateDefaultProject(@Valid @RequestBody UpdateDefaultProjectModel updateDefaultProjectModel, Authentication authentication) {
        var user = (UserDetailsImpl) authentication.getPrincipal();
        return updateDefaultProjectUseCase.updateDefaultProject(user.getId(), updateDefaultProjectModel.getNewDefaultProjectId())
                .map(projectModelMapper::toModel);
    }

    @PostMapping("/{projectId}/user")
    public void addUserToProject(@PathVariable long projectId, Authentication authentication) {
        var user = (UserDetailsImpl) authentication.getPrincipal();
        addUserToProjectUseCase.addUserToProject(user.getId(), projectId);
    }

    @DeleteMapping("/{projectId}/user")
    public void removeUserFromProject(@PathVariable long projectId, Authentication authentication) {
        var user = (UserDetailsImpl) authentication.getPrincipal();
        removeUserFromProjectUseCase.removeUserFromProject(user.getId(), projectId);
    }

    @DeleteMapping("/{projectId}")
    public void deleteProject(@PathVariable long projectId) {
        deleteProjectUseCase.deleteProject(projectId);
    }

    @PutMapping("/{projectId}")
    public ProjectModel updateProject(@Valid @RequestBody UpdateProjectModel updateProjectModel, @PathVariable long projectId) throws ProjectNameAlreadyInUseException, ProjectNotFoundException {
        return projectModelMapper.toModel(updateProjectUseCase.updateProject(new UpdateProjectUseCase.UpdateProjectCommand(
                projectId,
                updateProjectModel.getName(),
                updateProjectModel.getDateRange()
        )));
    }

    @GetMapping("/{projectId}/attributes")
    public List<String> getProjectAttributes(@PathVariable long projectId) {
        return getProjectAttributesUseCase.getProjectAttributes(projectId);
    }

    @GetMapping("/{projectId}/withDate")
    public ProjectWithDateModel getProjectWithDate(@PathVariable long projectId) {
        return projectModelMapper.toModel(getProjectWithDateUseCase.getProjectWithDate(projectId));
    }
}
