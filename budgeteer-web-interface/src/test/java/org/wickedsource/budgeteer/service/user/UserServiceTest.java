package org.wickedsource.budgeteer.service.user;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import de.adesso.budgeteer.core.user.PasswordHasher;
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
import org.wickedsource.budgeteer.service.UnknownEntityException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private ProjectRepository projectRepository;
  @Mock private VerificationTokenRepository verificationTokenRepository;
  @Mock private ForgotPasswordTokenRepository forgotPasswordTokenRepository;
  @Mock private PasswordHasher passwordHasher;
  @Mock UserMapper userMapper;
  @InjectMocks private UserService service;

  @Test
  void testRegisterUser() throws Exception {
    service.registerUser("User", "", "Password");
    verify(userRepository, times(1)).save(any(UserEntity.class));
  }

  @Test
  void testDuplicateUsernameDuringRegistration() {
    Assertions.assertThrows(
        UsernameAlreadyInUseException.class,
        () -> {
          when(userRepository.findByName("User")).thenReturn(null, new UserEntity());
          service.registerUser("User", "", "Password");
          service.registerUser("User", "", "Password");
        });
  }

  @Test
  void testLoginSuccess() throws Exception {
    var username = "username";
    var password = "password";
    var hashedPassword = "hashed-password";
    var userEntity = createUserEntity();
    given(passwordHasher.hash(password)).willReturn(hashedPassword);
    given(userRepository.findByNameOrMailAndPassword(username, hashedPassword))
        .willReturn(userEntity);
    given(userMapper.map(userEntity)).willReturn(new User());

    User user = service.login(username, password);

    Assertions.assertNotNull(user);
  }

  @Test
  void testLoginFail() {
    var user = "username";
    var password = "password";
    var hashedPassword = "hashed-password";
    when(passwordHasher.hash(password)).thenReturn(hashedPassword);
    when(userRepository.findByNameOrMailAndPassword(user, hashedPassword)).thenReturn(null);
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
    var correctPassword = "password";
    var incorrectPassword = "PASSWORD";
    var userEntity = createUserEntityOptional();
    when(passwordHasher.hash(correctPassword)).thenReturn(correctPassword);
    when(passwordHasher.hash(incorrectPassword)).thenReturn(incorrectPassword);
    when(userRepository.findById(1L)).thenReturn(userEntity);
    Assertions.assertTrue(service.checkPassword(1L, "password"));
    Assertions.assertFalse(service.checkPassword(1L, "PASSWORD"));
  }

  @Test
  void testResetPasswordMailNotFoundException() {
    var email = "nonuser@budgeteer.local";
    when(userRepository.findByMail(email)).thenReturn(null);
    Assertions.assertThrows(MailNotFoundException.class, () -> service.resetPassword(email));
  }

  @Test
  void testResetPasswordMailNotVerifiedException() {
    Assertions.assertThrows(
        MailNotVerifiedException.class,
        () -> {
          UserEntity user = createUserEntity();
          user.setMailVerified(false);
          when(userRepository.findByMail("user@budgeteer.local")).thenReturn(user);
          service.resetPassword("user@budgeteer.local");
        });
  }

  @Test
  void testLoadUserToEdit() {
    Optional<UserEntity> userMockOptional = createUserEntityOptional();
    when(userRepository.findById(1L)).thenReturn(userMockOptional);
    EditUserData user = service.loadUserToEdit(1L);
    UserEntity userMock = userMockOptional.get();
    Assertions.assertEquals(userMock.getId(), user.getId());
    Assertions.assertEquals(userMock.getMail(), user.getMail());
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
  void testSaveUserMailAlreadyInUseException() {
    Assertions.assertThrows(
        MailAlreadyInUseException.class,
        () -> {
          Optional<UserEntity> user = createUserEntityOptional();
          UserEntity user2 = createUserEntity();
          user2.setId(2L);
          user2.setMail("user2@budgeteer.local");
          when(userRepository.findById(1L)).thenReturn(user);
          EditUserData editUserData = service.loadUserToEdit(1L);
          when(userRepository.findByMail("user2@budgeteer.local")).thenReturn(user2);
          editUserData.setMail("user2@budgeteer.local");
          service.saveUser(editUserData, false);
        });
  }

  @Test
  void testSaveUser() throws MailAlreadyInUseException, UsernameAlreadyInUseException {
    Optional<UserEntity> user = createUserEntityOptional();
    when(userRepository.findById(1L)).thenReturn(user);
    EditUserData editUserData = service.loadUserToEdit(1L);
    editUserData.setName("user2");
    service.saveUser(editUserData, false);
    Assertions.assertEquals(editUserData.getName(), user.get().getName());
  }

  @Test
  void testCreateVerificationTokenForUser() {
    UserEntity user = createUserEntity();
    String uuid = UUID.randomUUID().toString();
    VerificationTokenEntity verificationTokenEntity =
        service.createVerificationTokenForUser(user, uuid);
    verify(verificationTokenRepository, times(1)).save(verificationTokenEntity);
  }

  @Test
  void testValidateVerificationTokenInvalid() {
    String uuid = UUID.randomUUID().toString();
    Assertions.assertEquals(-1, service.validateVerificationToken(uuid));
  }

  @Test
  void testValidateVerificationTokenExpired() {
    UserEntity user = createUserEntity();
    String uuid = UUID.randomUUID().toString();
    VerificationTokenEntity verificationTokenEntity = new VerificationTokenEntity(user, uuid);
    verificationTokenEntity.setExpiryDate(verificationTokenEntity.getExpiryDate().minusHours(25));
    when(verificationTokenRepository.findByToken(uuid)).thenReturn(verificationTokenEntity);
    Assertions.assertEquals(-2, service.validateVerificationToken(uuid));
  }

  @Test
  void testValidateVerificationTokenValid() {
    UserEntity user = createUserEntity();
    String uuid = UUID.randomUUID().toString();
    VerificationTokenEntity verificationTokenEntity = new VerificationTokenEntity(user, uuid);
    when(verificationTokenRepository.findByToken(uuid)).thenReturn(verificationTokenEntity);
    Assertions.assertEquals(0, service.validateVerificationToken(uuid));
  }

  @Test
  void getUserByMailMailNotFoundException() {
    var email = "nonuser@budgeteer.local";
    when(userRepository.findByMail(email)).thenReturn(null);
    Assertions.assertThrows(MailNotFoundException.class, () -> service.getUserByMail(email));
  }

  @Test
  void getUserByMail() throws MailNotFoundException {
    when(userRepository.findByMail("user@budgeteer.local")).thenReturn(createUserEntity());
    UserEntity user = service.getUserByMail("user@budgeteer.local");
    Assertions.assertNotNull(user);
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

  @Test
  void testCreateForgotPasswordTokenForUserWithOldToken() {
    UserEntity user = createUserEntity();
    String uuid = UUID.randomUUID().toString();
    ForgotPasswordTokenEntity oldForgotPasswordTokenEntity =
        new ForgotPasswordTokenEntity(user, uuid);
    when(forgotPasswordTokenRepository.findByUser(user)).thenReturn(oldForgotPasswordTokenEntity);
    ForgotPasswordTokenEntity newForgotPasswordTokenEntity =
        service.createForgotPasswordTokenForUser(user, uuid);
    verify(forgotPasswordTokenRepository, times(1)).delete(oldForgotPasswordTokenEntity);
    verify(forgotPasswordTokenRepository, times(1)).save(newForgotPasswordTokenEntity);
  }

  @Test
  void testValidateForgotPasswordTokenInvalid() {
    String uuid = UUID.randomUUID().toString();
    Assertions.assertEquals(-1, service.validateForgotPasswordToken(uuid));
  }

  @Test
  void testValidateForgotPasswordTokenExpired() {
    UserEntity user = createUserEntity();
    String uuid = UUID.randomUUID().toString();
    ForgotPasswordTokenEntity forgotPasswordTokenEntity = new ForgotPasswordTokenEntity(user, uuid);
    var date = forgotPasswordTokenEntity.getExpiryDate().minusHours(25);
    forgotPasswordTokenEntity.setExpiryDate(date);
    when(forgotPasswordTokenRepository.findByToken(uuid)).thenReturn(forgotPasswordTokenEntity);
    Assertions.assertEquals(-2, service.validateForgotPasswordToken(uuid));
  }

  @Test
  void testValidateForgotPasswordTokenValid() {
    UserEntity user = createUserEntity();
    String uuid = UUID.randomUUID().toString();
    ForgotPasswordTokenEntity forgotPasswordTokenEntity = new ForgotPasswordTokenEntity(user, uuid);
    when(forgotPasswordTokenRepository.findByToken(uuid)).thenReturn(forgotPasswordTokenEntity);
    Assertions.assertEquals(0, service.validateForgotPasswordToken(uuid));
  }

  @Test
  void testGetUserByForgotPasswordTokenNotNull() {
    UserEntity user = createUserEntity();
    String uuid = UUID.randomUUID().toString();
    ForgotPasswordTokenEntity forgotPasswordTokenEntity = new ForgotPasswordTokenEntity(user, uuid);
    when(forgotPasswordTokenRepository.findByToken(uuid)).thenReturn(forgotPasswordTokenEntity);
    UserEntity userResult = service.getUserByForgotPasswordToken(uuid);
    Assertions.assertNotNull(userResult);
  }

  @Test
  void testGetUserByForgotPasswordTokenNull() {
    String uuid = UUID.randomUUID().toString();
    UserEntity userResult = service.getUserByForgotPasswordToken(uuid);
    Assertions.assertNull(userResult);
  }

  @Test
  void testDeleteForgotPasswordToken() {
    UserEntity user = createUserEntity();
    String uuid = UUID.randomUUID().toString();
    ForgotPasswordTokenEntity forgotPasswordTokenEntity = new ForgotPasswordTokenEntity(user, uuid);
    when(forgotPasswordTokenRepository.findByToken(uuid)).thenReturn(forgotPasswordTokenEntity);
    service.deleteForgotPasswordToken(uuid);
    verify(forgotPasswordTokenRepository, times(1)).delete(forgotPasswordTokenEntity);
  }

  private UserEntity createUserEntity() {
    UserEntity user = new UserEntity();
    user.setId(1L);
    user.setName("user");
    user.setMail("user@budgeteer.local");
    user.setMailVerified(true);
    user.setPassword("password");
    user.setAuthorizedProjects(new ArrayList<>());
    return user;
  }

  private Optional<UserEntity> createUserEntityOptional() {
    UserEntity user = new UserEntity();
    user.setId(1L);
    user.setName("user");
    user.setMail("user@budgeteer.local");
    user.setMailVerified(true);
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
