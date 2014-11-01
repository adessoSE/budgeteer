package org.wickedsource.budgeteer.persistence.user;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wickedsource.budgeteer.persistence.RepositoryTestConfiguration;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;

import javax.transaction.Transactional;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RepositoryTestConfiguration.class})
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void testFindByNameAndPassword() {
        UserEntity user = new UserEntity();
        user.setName("name");
        user.setPassword("password");
        this.userRepository.save(user);

        UserEntity result = this.userRepository.findByNameAndPassword("name", "password");
        Assert.assertNotNull(result);
    }

    @Test
    public void testFindNotInProject() {
        ProjectEntity savedProject = createProjectAndUsers();

        List<UserEntity> usersNotInProject = userRepository.findNotInProject(savedProject.getId());
        Assert.assertEquals(1, usersNotInProject.size());
        Assert.assertEquals("user2", usersNotInProject.get(0).getName());
    }

    @Test
    public void testFindInProject() {
        ProjectEntity savedProject = createProjectAndUsers();

        List<UserEntity> usersInProject = userRepository.findInProject(savedProject.getId());
        Assert.assertEquals(1, usersInProject.size());
        Assert.assertEquals("user1", usersInProject.get(0).getName());
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
