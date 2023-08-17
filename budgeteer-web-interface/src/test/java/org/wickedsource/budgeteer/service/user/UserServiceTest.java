package org.wickedsource.budgeteer.service.user;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import de.adesso.budgeteer.persistence.project.ProjectEntity;
import de.adesso.budgeteer.persistence.project.ProjectRepository;
import de.adesso.budgeteer.persistence.user.*;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.wickedsource.budgeteer.service.UnknownEntityException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private ProjectRepository projectRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock UserMapper userMapper;
  @InjectMocks private UserService service;

  @Test
  void testRegisterUser() throws Exception {
    service.registerUser("User", "Password");
    verify(userRepository, times(1)).save(any(UserEntity.class));
  }

  @Test
  void testDuplicateUsernameDuringRegistration() {
    Assertions.assertThrows(
        UsernameAlreadyInUseException.class,
        () -> {
          when(userRepository.findByName("User")).thenReturn(null, new UserEntity());
          service.registerUser("User", "Password");
          service.registerUser("User", "Password");
        });
  }

  @Test
  void testLoginSuccess() throws Exception {
    var username = "username";
    var password = "password";
    var hashedPassword = "hashed-password";
    var userEntity = createUserEntity();
    userEntity.setPassword(hashedPassword);
    given(passwordEncoder.matches(password, hashedPassword)).willReturn(true);
    given(userRepository.findByName(username)).willReturn(userEntity);
    given(userMapper.map(userEntity)).willReturn(new User());

    User user = service.login(username, password);

    Assertions.assertNotNull(user);
  }

  @Test
  void testLoginFail() {
    var user = "username";
    var password = "password";
    Assertions.assertThrows(
        InvalidLoginCredentialsException.class, () -> service.login(user, password));
  }

  @Test
  void testAddUserToProjectSuccess() {
    when(userRepository.findById(1L)).thenReturn(createUserEntityOptional());
    when(projectRepository.findById(1L)).thenReturn(Optional.of(createProjectEntity()));
    service.addUserToProject(1L, 1L);
    // assertion not possible when mocking repository
  }

  @Test
  void testAddUserToProjectFailProjectNotFound() {
    var projectId = 1L;
    var userId = 2L;
    when(projectRepository.findById(projectId)).thenReturn(Optional.empty());
    Assertions.assertThrows(
        UnknownEntityException.class, () -> service.addUserToProject(projectId, userId));
  }

  @Test
  void testAddUserToProjectFailUserNotFound() {
    Assertions.assertThrows(
        UnknownEntityException.class,
        () -> {
          when(projectRepository.findById(1L)).thenReturn(Optional.of(createProjectEntity()));
          service.addUserToProject(1L, 1L);
        });
  }

  @Test
  void testRemoveUserFromProjectSuccess() {
    when(userRepository.findById(1L)).thenReturn(createUserEntityOptional());
    when(projectRepository.findById(1L)).thenReturn(Optional.of(createProjectEntity()));
    service.removeUserFromProject(1L, 1L);
    // assertion not possible when mocking repository
  }

  @Test
  void testRemoveUserFromProjectFailProjectNotFound() {
    var projectId = 1L;
    var userId = 2L;
    when(projectRepository.findById(1L)).thenReturn(Optional.empty());
    Assertions.assertThrows(
        UnknownEntityException.class, () -> service.removeUserFromProject(projectId, userId));
  }

  @Test
  void testRemoveUserFromProjectFailUserNotFound() {
    Assertions.assertThrows(
        UnknownEntityException.class,
        () -> {
          when(projectRepository.findById(1L)).thenReturn(Optional.of(createProjectEntity()));
          service.removeUserFromProject(1L, 1L);
        });
  }

  @Test
  void testGetUsersNotInProject() {
    var userEntities = List.of(createUserEntity());
    var name = "user";
    var user = new User();
    user.setName(name);
    List<User> expectedUsers = List.of(user);
    when(userRepository.findNotInProject(1L)).thenReturn(userEntities);
    when(userMapper.map(userEntities)).thenReturn(expectedUsers);
    List<User> users = service.getUsersNotInProject(1L);
    Assertions.assertEquals(1, users.size());
    Assertions.assertEquals(name, users.get(0).getName());
  }

  @Test
  void testGetUsersInProject() {
    var name = "user";
    var userEntities = List.of(createUserEntity());
    var expectedUser = new User();
    expectedUser.setName(name);
    when(userRepository.findInProject(1L)).thenReturn(userEntities);
    when(userMapper.map(userEntities)).thenReturn(List.of(expectedUser));
    List<User> users = service.getUsersInProject(1L);
    Assertions.assertEquals(1, users.size());
    Assertions.assertEquals("user", users.get(0).getName());
  }

  @Test
  void testCheckPassword() {
    var correctPassword = "hashed-password";
    var incorrectPassword = "PASSWORD";
    var userEntity = createUserEntityOptional();
    userEntity.ifPresent(user -> user.setPassword(correctPassword));
    when(passwordEncoder.matches(correctPassword, "hashed-password")).thenReturn(true);
    when(passwordEncoder.matches(incorrectPassword, "hashed-password")).thenReturn(false);
    when(userRepository.findById(1L)).thenReturn(userEntity);
    Assertions.assertTrue(service.checkPassword(1L, correctPassword));
    Assertions.assertFalse(service.checkPassword(1L, incorrectPassword));
  }

  @Test
  void testLoadUserToEdit() {
    Optional<UserEntity> userMockOptional = createUserEntityOptional();
    when(userRepository.findById(1L)).thenReturn(userMockOptional);
    EditUserData user = service.loadUserToEdit(1L);
    UserEntity userMock = userMockOptional.get();
    Assertions.assertEquals(userMock.getId(), user.getId());
    Assertions.assertEquals(userMock.getName(), user.getName());
    Assertions.assertEquals(userMock.getPassword(), user.getPassword());
  }

  @Test
  void testSaveUserUsernameAlreadyInUseException() {
    Assertions.assertThrows(
        UsernameAlreadyInUseException.class,
        () -> {
          Optional<UserEntity> user = createUserEntityOptional();
          UserEntity user2 = createUserEntity();
          user2.setId(2L);
          user2.setName("user2");
          when(userRepository.findById(1L)).thenReturn(user);
          EditUserData editUserData = service.loadUserToEdit(1L);
          when(userRepository.findByName("user2")).thenReturn(user2);
          editUserData.setName("user2");
          service.saveUser(editUserData, false);
        });
  }

  @Test
  void testSaveUser() throws UsernameAlreadyInUseException {
    Optional<UserEntity> user = createUserEntityOptional();
    when(userRepository.findById(1L)).thenReturn(user);
    EditUserData editUserData = service.loadUserToEdit(1L);
    editUserData.setName("user2");
    service.saveUser(editUserData, false);
    Assertions.assertEquals(editUserData.getName(), user.get().getName());
  }

  @Test
  void getUserByIdUserIdNotFoundException() {
    var userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.empty());
    Assertions.assertThrows(UserIdNotFoundException.class, () -> service.getUserById(userId));
  }

  @Test
  void getUserById() throws UserIdNotFoundException {
    when(userRepository.findById(1L)).thenReturn(createUserEntityOptional());
    UserEntity user = service.getUserById(1L);
    Assertions.assertNotNull(user);
  }

  private UserEntity createUserEntity() {
    UserEntity user = new UserEntity();
    user.setId(1L);
    user.setName("user");
    user.setPassword("password");
    user.setAuthorizedProjects(new ArrayList<>());
    return user;
  }

  private Optional<UserEntity> createUserEntityOptional() {
    UserEntity user = new UserEntity();
    user.setId(1L);
    user.setName("user");
    user.setPassword("password");
    user.setAuthorizedProjects(new ArrayList<>());
    return Optional.of(user);
  }

  private ProjectEntity createProjectEntity() {
    ProjectEntity project = new ProjectEntity();
    project.setId(1L);
    project.setName("name");
    project.setAuthorizedUsers(new ArrayList<>());
    return project;
  }
}
