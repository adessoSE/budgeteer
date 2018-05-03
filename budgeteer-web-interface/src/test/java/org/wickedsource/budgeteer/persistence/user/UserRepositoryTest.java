package org.wickedsource.budgeteer.persistence.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;

import java.util.List;

class UserRepositoryTest extends IntegrationTestTemplate {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void testFindByNameAndPassword() {
        UserEntity user = new UserEntity();
        user.setName("name");
        user.setPassword("password");
        this.userRepository.save(user);

        UserEntity result = this.userRepository.findByNameAndPassword("name", "password");
        Assertions.assertNotNull(result);
    }

    @Test
    void testFindNotInProject() {
        ProjectEntity savedProject = createProjectAndUsers();

        List<UserEntity> usersNotInProject = userRepository.findNotInProject(savedProject.getId());
        Assertions.assertEquals(1, usersNotInProject.size());
        Assertions.assertEquals("user2", usersNotInProject.get(0).getName());
    }

    @Test
    void testFindInProject() {
        ProjectEntity savedProject = createProjectAndUsers();

        List<UserEntity> usersInProject = userRepository.findInProject(savedProject.getId());
        Assertions.assertEquals(1, usersInProject.size());
        Assertions.assertEquals("user1", usersInProject.get(0).getName());
    }

    private ProjectEntity createProjectAndUsers() {
        ProjectEntity project = new ProjectEntity();
        project.setName("name");
        ProjectEntity savedProject = projectRepository.save(project);

        UserEntity user1 = new UserEntity();
        user1.setName("user1");
        user1.setPassword("password");
        user1.getAuthorizedProjects().add(project);
        project.getAuthorizedUsers().add(user1);
        userRepository.save(user1);

        UserEntity user2 = new UserEntity();
        user2.setName("user2");
        user2.setPassword("password");
        userRepository.save(user2);
        return savedProject;
    }

}
