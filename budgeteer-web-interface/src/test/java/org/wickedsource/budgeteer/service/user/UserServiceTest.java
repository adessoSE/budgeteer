package org.wickedsource.budgeteer.service.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;
import org.wickedsource.budgeteer.persistence.user.UserEntity;
import org.wickedsource.budgeteer.persistence.user.UserRepository;
import org.wickedsource.budgeteer.service.ServiceTestTemplate;
import org.wickedsource.budgeteer.service.UnknownEntityException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class UserServiceTest extends ServiceTestTemplate{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserService service;

    @Autowired
    private PasswordHasher passwordHasher;

    @Test
    void testRegisterUser() throws Exception{
        service.registerUser("User", "Password");
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testDuplicateUsernameDuringRegistration() {
        Assertions.assertThrows(UsernameAlreadyInUseException.class, () -> {
            when(userRepository.findByName("User")).thenReturn(null, new UserEntity());
            service.registerUser("User", "Password");
            service.registerUser("User", "Password");
        });
    }

    @Test
    void testLoginSuccess() throws Exception {
        when(userRepository.findByNameAndPassword("user", passwordHasher.hash("password"))).thenReturn(createUserEntity());
        User user = service.login("user", "password");
        Assertions.assertNotNull(user);
    }

    @Test
    void testLoginFail() {
        Assertions.assertThrows(InvalidLoginCredentialsException.class, () -> {
            when(userRepository.findByNameAndPassword("user", passwordHasher.hash("password"))).thenReturn(null);
            service.login("user", "password");
        });
    }

    @Test
    void testAddUserToProjectSuccess() {
        when(userRepository.findOne(1L)).thenReturn(createUserEntity());
        when(projectRepository.findOne(1L)).thenReturn(createProjectEntity());
        service.addUserToProject(1L, 1L);
        // assertion not possible when mocking repository
    }

    @Test
    void testAddUserToProjectFailProjectNotFound() {
        Assertions.assertThrows(UnknownEntityException.class, () -> {
            when(userRepository.findOne(1L)).thenReturn(createUserEntity());
            service.addUserToProject(1L, 1L);
        });
    }

    @Test
    void testAddUserToProjectFailUserNotFound() {
        Assertions.assertThrows(UnknownEntityException.class, () -> {
            when(projectRepository.findOne(1L)).thenReturn(createProjectEntity());
            service.addUserToProject(1L, 1L);
        });
    }

    @Test
    void testRemoveUserFromProjectSuccess() {
        when(userRepository.findOne(1L)).thenReturn(createUserEntity());
        when(projectRepository.findOne(1L)).thenReturn(createProjectEntity());
        service.removeUserFromProject(1L, 1L);
        // assertion not possible when mocking repository
    }

    @Test
    void testRemoveUserFromProjectFailProjectNotFound() {
        Assertions.assertThrows(UnknownEntityException.class, () -> {
            when(userRepository.findOne(1L)).thenReturn(createUserEntity());
            service.removeUserFromProject(1L, 1L);
        });
    }

    @Test
    void testRemoveUserFromProjectFailUserNotFound() {
        Assertions.assertThrows(UnknownEntityException.class, () -> {
            when(projectRepository.findOne(1L)).thenReturn(createProjectEntity());
            service.removeUserFromProject(1L, 1L);
        });
    }

    @Test
    void testGetUsersNotInProject() {
        when(userRepository.findNotInProject(1L)).thenReturn(Arrays.asList(createUserEntity()));
        List<User> users = service.getUsersNotInProject(1L);
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals("user", users.get(0).getName());
    }

    @Test
    void testGetUsersInProject() {
        when(userRepository.findInProject(1L)).thenReturn(Arrays.asList(createUserEntity()));
        List<User> users = service.getUsersInProject(1L);
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals("user", users.get(0).getName());
    }

    private UserEntity createUserEntity() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("user");
        user.setPassword(passwordHasher.hash("password"));
        user.setAuthorizedProjects(new ArrayList<ProjectEntity>());
        return user;
    }

    private ProjectEntity createProjectEntity() {
        ProjectEntity project = new ProjectEntity();
        project.setId(1L);
        project.setName("name");
        project.setAuthorizedUsers(new ArrayList<UserEntity>());
        return project;
    }
}
