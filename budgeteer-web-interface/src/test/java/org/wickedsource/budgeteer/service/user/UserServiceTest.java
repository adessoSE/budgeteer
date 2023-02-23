package org.wickedsource.budgeteer.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

import de.adesso.budgeteer.core.exception.NotFoundException;
import de.adesso.budgeteer.core.project.domain.Project;
import de.adesso.budgeteer.core.project.port.in.AddUserToProjectUseCase;
import de.adesso.budgeteer.core.project.port.in.RemoveUserFromProjectUseCase;
import de.adesso.budgeteer.core.user.*;
import de.adesso.budgeteer.core.user.domain.UserWithEmail;
import de.adesso.budgeteer.core.user.port.in.*;
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

  @Mock private PasswordHasher passwordHasher;

  @Mock private GetUsersInProjectUseCase getUsersInProjectUseCase;
  @Mock private GetUsersNotInProjectUseCase getUsersNotInProjectUseCase;
  @Mock private RemoveUserFromProjectUseCase removeUserFromProjectUseCase;
  @Mock private AddUserToProjectUseCase addUserToProjectUseCase;
  @Mock private LoginUseCase loginUseCase;
  @Mock private RegisterUseCase registerUseCase;
  @Mock private GetUserWithEmailUseCase getUserWithEmailUseCase;
  @Mock private UpdateUserUseCase updateUserUseCase;
  @Mock private VerifyEmailUseCase verifyEmailUseCase;
  @Mock private ResetPasswordUseCase resetPasswordUseCase;
  @Mock UserMapper userMapper;
  @InjectMocks private UserService service;

  @Test
  void testRegisterUser() throws Exception {
    var username = "User";
    var password = "Password";
    var hashedPassword = "asdf;jkas";
    when(passwordHasher.hash(password)).thenReturn(hashedPassword);

    service.registerUser(username, "", password);

    verify(registerUseCase, times(1))
        .register(new RegisterUseCase.RegisterCommand(username, "", hashedPassword));
  }

  @Test
  void testDuplicateUsernameDuringRegistration() throws UserException {
    var exception = new UserException();
    exception.addCause(UserException.UserErrors.USERNAME_ALREADY_IN_USE);
    var username = "username";
    var hashedPassword = ";djaks;df";
    when(passwordHasher.hash(any())).thenReturn(hashedPassword);
    doThrow(exception)
        .when(registerUseCase)
        .register(new RegisterUseCase.RegisterCommand(username, "", hashedPassword));

    assertThatExceptionOfType(UsernameAlreadyInUseException.class)
        .isThrownBy(() -> service.registerUser(username, "", "password"));
  }

  @Test
  void testLoginSuccess() throws Exception {
    var username = "username";
    var password = "password";
    var hashedPassword = "hashed-password";
    var user = new de.adesso.budgeteer.core.user.domain.User(1L, username);
    when(passwordHasher.hash(password)).thenReturn(hashedPassword);
    when(loginUseCase.login(username, hashedPassword)).thenReturn(user);
    when(userMapper.map(user)).thenReturn(new User());

    var webUser = service.login(username, password);

    Assertions.assertNotNull(webUser);
  }

  @Test
  void testLoginFail() throws de.adesso.budgeteer.core.user.InvalidLoginCredentialsException {
    var username = "username";
    var password = "password";
    var hashedPassword = "hashed-password";
    when(passwordHasher.hash(password)).thenReturn(hashedPassword);
    doThrow(de.adesso.budgeteer.core.user.InvalidLoginCredentialsException.class)
        .when(loginUseCase)
        .login(username, hashedPassword);

    Assertions.assertThrows(
        InvalidLoginCredentialsException.class, () -> service.login(username, password));
  }

  @Test
  void testAddUserToProjectSuccess() throws NotFoundException {
    var projectId = 1L;
    var userId = 2L;
    doNothing().when(addUserToProjectUseCase).addUserToProject(userId, projectId);

    service.addUserToProject(projectId, userId);

    verify(addUserToProjectUseCase).addUserToProject(userId, projectId);
  }

  @Test
  void testAddUserToProjectFailProjectNotFound() throws NotFoundException {
    var projectId = 1L;
    var userId = 2L;
    doThrow(new NotFoundException(Project.class))
        .when(addUserToProjectUseCase)
        .addUserToProject(userId, projectId);

    assertThatExceptionOfType(UnknownEntityException.class)
        .isThrownBy(() -> service.addUserToProject(projectId, userId));
  }

  @Test
  void testAddUserToProjectFailUserNotFound() throws NotFoundException {
    var projectId = 1L;
    var userId = 2L;
    doThrow(new NotFoundException(de.adesso.budgeteer.core.user.domain.User.class))
        .when(addUserToProjectUseCase)
        .addUserToProject(userId, projectId);

    assertThatExceptionOfType(UnknownEntityException.class)
        .isThrownBy(() -> service.addUserToProject(projectId, userId));
  }

  @Test
  void testRemoveUserFromProjectSuccess() throws NotFoundException {
    var projectId = 1L;
    var userId = 2L;
    doNothing().when(removeUserFromProjectUseCase).removeUserFromProject(userId, projectId);

    service.removeUserFromProject(projectId, userId);

    verify(removeUserFromProjectUseCase).removeUserFromProject(userId, projectId);
  }

  @Test
  void testRemoveUserFromProjectFailProjectNotFound() throws NotFoundException {
    var projectId = 1L;
    var userId = 2L;
    doThrow(new NotFoundException(Project.class))
        .when(removeUserFromProjectUseCase)
        .removeUserFromProject(userId, projectId);

    assertThatExceptionOfType(UnknownEntityException.class)
        .isThrownBy(() -> service.removeUserFromProject(projectId, userId));
  }

  @Test
  void testRemoveUserFromProjectFailUserNotFound() throws NotFoundException {
    var projectId = 1L;
    var userId = 2L;
    doThrow(new NotFoundException(de.adesso.budgeteer.core.user.domain.User.class))
        .when(removeUserFromProjectUseCase)
        .removeUserFromProject(userId, projectId);

    assertThatExceptionOfType(UnknownEntityException.class)
        .isThrownBy(() -> service.removeUserFromProject(projectId, userId));
  }

  @Test
  void testGetUsersNotInProject() {
    var projectId = 1L;
    var user = new de.adesso.budgeteer.core.user.domain.User(1L, "name");
    var users = List.of(user);
    when(getUsersNotInProjectUseCase.getUsersNotInProject(projectId)).thenReturn(users);
    var name = "user";
    var webUser = new User();
    webUser.setName(name);
    when(userMapper.map(user)).thenReturn(webUser);

    var result = service.getUsersNotInProject(1L);

    assertThat(result).containsExactly(webUser);
  }

  @Test
  void testGetUsersInProject() {
    var projectId = 1L;
    var user = new de.adesso.budgeteer.core.user.domain.User(1L, "name");
    var users = List.of(user);
    when(getUsersNotInProjectUseCase.getUsersNotInProject(projectId)).thenReturn(users);
    var name = "user";
    var webUser = new User();
    webUser.setName(name);
    when(userMapper.map(user)).thenReturn(webUser);

    var result = service.getUsersNotInProject(1L);

    assertThat(result).containsExactly(webUser);
  }

  @Test
  void checkPasswordShouldReturnTrueIfPasswordMatches()
      throws de.adesso.budgeteer.core.user.InvalidLoginCredentialsException {
    var userId = 1L;
    var username = "username";
    var password = "password";
    when(getUserWithEmailUseCase.getUserWithEmail(userId))
        .thenReturn(new UserWithEmail(userId, username, ""));
    when(passwordHasher.hash(password)).thenReturn(password);
    when(loginUseCase.login(username, password)).thenReturn(null);

    var result = service.checkPassword(userId, password);

    assertThat(result).isTrue();
  }

  @Test
  void checkPasswordShouldReturnFalseIfPasswordIsWrong()
      throws de.adesso.budgeteer.core.user.InvalidLoginCredentialsException {
    var userId = 1L;
    var username = "username";
    var password = "password";
    when(getUserWithEmailUseCase.getUserWithEmail(userId))
        .thenReturn(new UserWithEmail(userId, username, ""));
    when(passwordHasher.hash(password)).thenReturn(password);
    doThrow(de.adesso.budgeteer.core.user.InvalidLoginCredentialsException.class)
        .when(loginUseCase)
        .login(username, password);

    var result = service.checkPassword(userId, password);

    assertThat(result).isFalse();
  }

  @Test
  void testResetPasswordMailNotFoundException()
      throws de.adesso.budgeteer.core.user.MailNotFoundException, MailNotEnabledException,
          de.adesso.budgeteer.core.user.MailNotVerifiedException {
    var email = "nonuser@budgeteer.local";
    doThrow(de.adesso.budgeteer.core.user.MailNotFoundException.class)
        .when(resetPasswordUseCase)
        .resetPassword(email);

    Assertions.assertThrows(MailNotFoundException.class, () -> service.resetPassword(email));
  }

  @Test
  void testResetPasswordMailNotVerifiedException()
      throws de.adesso.budgeteer.core.user.MailNotFoundException, MailNotEnabledException,
          de.adesso.budgeteer.core.user.MailNotVerifiedException {
    var email = "user@budgeteer.local";
    doThrow(de.adesso.budgeteer.core.user.MailNotVerifiedException.class)
        .when(resetPasswordUseCase)
        .resetPassword(email);
    Assertions.assertThrows(MailNotVerifiedException.class, () -> service.resetPassword(email));
  }

  @Test
  void testLoadUserToEdit() {
    var userWithMail = new UserWithEmail(1L, "username", "");
    when(getUserWithEmailUseCase.getUserWithEmail(userWithMail.getId())).thenReturn(userWithMail);
    var expected = new EditUserData();
    expected.setId(userWithMail.getId());
    expected.setName(userWithMail.getName());
    expected.setMail(userWithMail.getEmail());

    var result = service.loadUserToEdit(userWithMail.getId());

    assertThat(result).isEqualTo(expected);
  }

  @Test
  void testSaveUserUsernameAlreadyInUseException()
      throws de.adesso.budgeteer.core.user.UsernameAlreadyInUseException,
          de.adesso.budgeteer.core.user.InvalidLoginCredentialsException,
          de.adesso.budgeteer.core.user.MailAlreadyInUseException {
    var editUserData = new EditUserData();
    editUserData.setId(1L);
    editUserData.setName("name");
    editUserData.setMail("mail");
    editUserData.setPassword("old-password");
    editUserData.setNewPassword("new-password");
    when(passwordHasher.hash(any())).thenAnswer(invocation -> invocation.getArgument(0));
    doThrow(de.adesso.budgeteer.core.user.UsernameAlreadyInUseException.class)
        .when(updateUserUseCase)
        .updateUser(
            new UpdateUserUseCase.UpdateUserCommand(
                editUserData.getId(),
                editUserData.getName(),
                editUserData.getMail(),
                editUserData.getPassword(),
                editUserData.getNewPassword()));

    assertThatExceptionOfType(UsernameAlreadyInUseException.class)
        .isThrownBy(() -> service.saveUser(editUserData));
  }

  @Test
  void testSaveUserMailAlreadyInUseException()
      throws de.adesso.budgeteer.core.user.UsernameAlreadyInUseException,
          de.adesso.budgeteer.core.user.InvalidLoginCredentialsException,
          de.adesso.budgeteer.core.user.MailAlreadyInUseException {
    var editUserData = new EditUserData();
    editUserData.setId(1L);
    editUserData.setName("name");
    editUserData.setMail("mail");
    editUserData.setPassword("old-password");
    editUserData.setNewPassword("new-password");
    when(passwordHasher.hash(any())).thenAnswer(invocation -> invocation.getArgument(0));
    doThrow(de.adesso.budgeteer.core.user.MailAlreadyInUseException.class)
        .when(updateUserUseCase)
        .updateUser(
            new UpdateUserUseCase.UpdateUserCommand(
                editUserData.getId(),
                editUserData.getName(),
                editUserData.getMail(),
                editUserData.getPassword(),
                editUserData.getNewPassword()));

    assertThatExceptionOfType(MailAlreadyInUseException.class)
        .isThrownBy(() -> service.saveUser(editUserData));
  }

  @Test
  void testSaveUser()
      throws de.adesso.budgeteer.core.user.UsernameAlreadyInUseException,
          de.adesso.budgeteer.core.user.InvalidLoginCredentialsException,
          de.adesso.budgeteer.core.user.MailAlreadyInUseException, UsernameAlreadyInUseException,
          MailAlreadyInUseException {
    var editUserData = new EditUserData();
    editUserData.setId(1L);
    editUserData.setName("name");
    editUserData.setMail("mail");
    editUserData.setPassword("old-password");
    editUserData.setNewPassword("new-password");
    when(passwordHasher.hash(any())).thenAnswer(invocation -> invocation.getArgument(0));
    var command =
        new UpdateUserUseCase.UpdateUserCommand(
            editUserData.getId(),
            editUserData.getName(),
            editUserData.getMail(),
            editUserData.getPassword(),
            editUserData.getNewPassword());
    doNothing().when(updateUserUseCase).updateUser(command);

    service.saveUser(editUserData);

    verify(updateUserUseCase).updateUser(command);
  }

  @Test
  void testValidateVerificationTokenInvalid()
      throws ExpiredVerificationTokenException, InvalidVerificationTokenException {
    String uuid = UUID.randomUUID().toString();
    doThrow(InvalidVerificationTokenException.class).when(verifyEmailUseCase).verifyEmail(uuid);

    Assertions.assertEquals(-1, service.validateVerificationToken(uuid));
  }

  @Test
  void testValidateVerificationTokenExpired()
      throws ExpiredVerificationTokenException, InvalidVerificationTokenException {
    String uuid = UUID.randomUUID().toString();
    doThrow(ExpiredVerificationTokenException.class).when(verifyEmailUseCase).verifyEmail(uuid);

    Assertions.assertEquals(-2, service.validateVerificationToken(uuid));
  }

  @Test
  void testValidateVerificationTokenValid()
      throws ExpiredVerificationTokenException, InvalidVerificationTokenException {
    String uuid = UUID.randomUUID().toString();
    doNothing().when(verifyEmailUseCase).verifyEmail(uuid);

    Assertions.assertEquals(0, service.validateVerificationToken(uuid));
  }
}
