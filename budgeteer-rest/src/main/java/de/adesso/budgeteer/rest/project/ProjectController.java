package de.adesso.budgeteer.rest.project;

import de.adesso.budgeteer.core.project.ProjectNameAlreadyInUseException;
import de.adesso.budgeteer.core.project.port.in.*;
import de.adesso.budgeteer.rest.project.model.CreateProjectModel;
import de.adesso.budgeteer.rest.project.model.ProjectModel;
import de.adesso.budgeteer.rest.project.model.UpdateDefaultProjectModel;
import de.adesso.budgeteer.rest.security.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
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
    private final ProjectModelMapper projectModelMapper;

    @PostMapping
    public ProjectModel createProject(@Valid @RequestBody CreateProjectModel createProjectModel, Authentication authentication) throws ProjectNameAlreadyInUseException {
        var user = (UserDetailsImpl) authentication.getPrincipal();
        return projectModelMapper.toModel(createProjectUseCase.createProject(new CreateProjectUseCase.CreateProjectCommand(createProjectModel.getName(), user.getId())));
    }

    @GetMapping("/{projectId}")
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
}
