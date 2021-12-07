package de.adesso.budgeteer.rest.project;

import de.adesso.budgeteer.core.project.ProjectException;
import de.adesso.budgeteer.core.project.port.in.*;
import de.adesso.budgeteer.rest.project.exceptions.CreateProjectException;
import de.adesso.budgeteer.rest.project.exceptions.UpdateProjectException;
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
    public ProjectModel createProject(@Valid @RequestBody CreateProjectModel createProjectModel, Authentication authentication) {
        var user = (UserDetailsImpl) authentication.getPrincipal();
        try {
            return projectModelMapper.toModel(createProjectUseCase.createProject(new CreateProjectUseCase.CreateProjectCommand(createProjectModel.getName(), user.getId())));
        } catch (ProjectException e) {
            throw new CreateProjectException(e);
        }
    }

    @GetMapping("/{projectId}")
    @PreAuthorize("userHasAccessToProject(#projectId)")
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
    @PreAuthorize("userHasAccessToProject(#projectId)")
    public void addUserToProject(@PathVariable long projectId, @RequestParam long userId) {
        addUserToProjectUseCase.addUserToProject(userId, projectId);
    }

    @DeleteMapping("/{projectId}/user")
    @PreAuthorize("userHasAccessToProject(#projectId)")
    public void removeUserFromProject(@PathVariable long projectId, @RequestParam long userId) {
        removeUserFromProjectUseCase.removeUserFromProject(userId, projectId);
    }

    @DeleteMapping("/{projectId}")
    @PreAuthorize("userHasAccessToProject(#projectId)")
    public void deleteProject(@PathVariable long projectId) {
        deleteProjectUseCase.deleteProject(projectId);
    }

    @PutMapping("/{projectId}")
    @PreAuthorize("userHasAccessToProject(#projectId)")
    public ProjectModel updateProject(@Valid @RequestBody UpdateProjectModel updateProjectModel, @PathVariable long projectId) {
        try {
            return projectModelMapper.toModel(updateProjectUseCase.updateProject(new UpdateProjectUseCase.UpdateProjectCommand(
                    projectId,
                    updateProjectModel.getName(),
                    updateProjectModel.getDateRange()
            )));
        } catch (ProjectException e) {
            throw new UpdateProjectException(e);
        }
    }

    @GetMapping("/{projectId}/attributes")
    @PreAuthorize("userHasAccessToProject(#projectId)")
    public List<String> getProjectAttributes(@PathVariable long projectId) {
        return getProjectAttributesUseCase.getProjectAttributes(projectId);
    }

    @GetMapping("/{projectId}/withDate")
    @PreAuthorize("userHasAccessToProject(#projectId)")
    public ProjectWithDateModel getProjectWithDate(@PathVariable long projectId) {
        return projectModelMapper.toModel(getProjectWithDateUseCase.getProjectWithDate(projectId));
    }
}
