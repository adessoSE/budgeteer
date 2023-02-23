package org.wickedsource.budgeteer.service.project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import de.adesso.budgeteer.core.project.domain.Project;
import de.adesso.budgeteer.core.project.port.in.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

  @Mock private ProjectBaseDataMapper mapper;
  @Mock private CreateProjectUseCase createProjectUseCase;
  @Mock private GetUsersProjectsUseCase getUsersProjectsUseCase;
  @Mock private GetDefaultProjectUseCase getDefaultProjectUseCase;
  @Mock private DeleteProjectUseCase deleteProjectUseCase;
  @Mock private UpdateDefaultProjectUseCase updateDefaultProjectUseCase;
  @Mock private GetProjectWithDateUseCase getProjectWithDateUseCase;
  @Mock private UpdateProjectUseCase updateProjectUseCase;
  @InjectMocks private ProjectService projectService;

  @Test
  void testCreateProject() throws Exception {
    var name = "project-name";
    var userId = 1L;
    var project = new Project(1L, name);
    var expected = new ProjectBaseData();
    expected.setId(project.getId());
    expected.setName(project.getName());
    when(createProjectUseCase.createProject(
            new CreateProjectUseCase.CreateProjectCommand(name, userId)))
        .thenReturn(project);
    when(mapper.map(project)).thenReturn(expected);

    var result = projectService.createProject(name, userId);

    assertThat(result).isEqualTo(expected);
  }

  @Test
  void testGetProjectsForUser() {
    var userId = 1L;
    var project = new Project(1L, "name");
    var expected = new ProjectBaseData();
    expected.setId(project.getId());
    expected.setName(project.getName());
    when(getUsersProjectsUseCase.getUsersProjects(userId)).thenReturn(List.of(project));
    when(mapper.map(project)).thenReturn(expected);

    var result = projectService.getProjectsForUser(1L);

    assertThat(result).containsExactly(expected);
  }

  @Test
  void testDeleteProject() {
    var projectId = 1L;
    doNothing().when(deleteProjectUseCase).deleteProject(projectId);

    projectService.deleteProject(projectId);

    verify(deleteProjectUseCase).deleteProject(projectId);
  }
}
