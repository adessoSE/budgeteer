package org.wickedsource.budgeteer.service.project;

import static org.mockito.Mockito.*;

import de.adesso.budgeteer.persistence.project.ProjectEntity;
import de.adesso.budgeteer.persistence.project.ProjectRepository;
import de.adesso.budgeteer.persistence.user.UserEntity;
import de.adesso.budgeteer.persistence.user.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.wickedsource.budgeteer.service.ServiceTestTemplate;

class ProjectServiceTest extends ServiceTestTemplate {

  @Autowired private ProjectService projectService;
  @MockBean private ProjectRepository projectRepository;
  @MockBean private UserRepository userRepository;

  @Test
  void testCreateProject() throws Exception {
    when(projectRepository.save(any(ProjectEntity.class))).thenReturn(createProjectEntity());
    when(userRepository.findById(anyLong())).thenReturn(createUserWithProjects());
    ProjectBaseData project = projectService.createProject("MyProject", 1L);
    verify(projectRepository, times(1)).save(any(ProjectEntity.class));
    Assertions.assertEquals("name", project.getName());
  }

  @Test
  void testGetProjectsForUser() throws Exception {
    when(userRepository.findById(1L)).thenReturn(createUserWithProjects());
    List<ProjectBaseData> projects = projectService.getProjectsForUser(1L);
    Assertions.assertEquals(2, projects.size());
  }

  private Optional<UserEntity> createUserWithProjects() {
    UserEntity user = new UserEntity();
    user.setId(1L);
    user.setName("user");
    user.setPassword("password");
    user.getAuthorizedProjects().add(createProjectEntity());
    user.getAuthorizedProjects().add(createProjectEntity());
    return Optional.of(user);
  }

  private ProjectEntity createProjectEntity() {
    ProjectEntity project = new ProjectEntity();
    project.setId(1L);
    project.setName("name");
    return project;
  }

  @Test
  void testDeleteProject() throws Exception {
    projectService.deleteProject(1L);
    verify(projectRepository, times(1)).deleteById(1L);
  }
}
