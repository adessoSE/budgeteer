package org.wickedsource.budgeteer.service.project;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;
import org.wickedsource.budgeteer.persistence.user.UserEntity;
import org.wickedsource.budgeteer.persistence.user.UserRepository;
import org.wickedsource.budgeteer.service.ServiceTestTemplate;

import java.util.List;

import static org.mockito.Mockito.*;

public class ProjectServiceTest extends ServiceTestTemplate {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateProject() throws Exception {
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(createProjectEntity());
        ProjectBaseData project = projectService.createProject("MyProject");
        verify(projectRepository).save(any(ProjectEntity.class));
        Assert.assertEquals("name", project.getName());
    }

    @Test
    public void testGetProjectsForUser() throws Exception {
        when(userRepository.findOne(1l)).thenReturn(createUserWithProjects());
        List<ProjectBaseData> projects = projectService.getProjectsForUser(1l);
        Assert.assertEquals(2, projects.size());
    }

    private UserEntity createUserWithProjects() {
        UserEntity user = new UserEntity();
        user.setId(1l);
        user.setName("user");
        user.setPassword("password");
        user.getAuthorizedProjects().add(createProjectEntity());
        user.getAuthorizedProjects().add(createProjectEntity());
        return user;
    }

    private ProjectEntity createProjectEntity() {
        ProjectEntity project = new ProjectEntity();
        project.setId(1l);
        project.setName("name");
        return project;
    }

    @Test
    public void testDeleteProject() throws Exception {
        projectService.deleteProject(1l);
        verify(projectRepository, times(1)).delete(1l);
    }
}
