package de.adesso.budgeteer.core.user.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

import de.adesso.budgeteer.core.user.*;
import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.core.user.port.in.UpdateUserUseCase;
import de.adesso.budgeteer.core.user.port.out.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateUserServiceTest {

  @InjectMocks private UpdateUserService updateUserService;
  @Mock private UserWithNameExistsPort userWithNameExistsPort;
  @Mock private GetUserByIdPort getUserByIdPort;
  @Mock private PasswordMatchesPort passwordMatchesPort;
  @Mock private UpdateUserPort updateUserPort;
  @Mock private PasswordHasher passwordHasher;

  @Test
  void shouldThrowNullPointerExceptionIfUsernameIsNull() {
    var command = new UpdateUserUseCase.UpdateUserCommand(1, null, null, null);
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> updateUserService.updateUser(command));
  }

  @Test
  void shouldThrowUsernameAlreadyInUseExceptionIfNewUsernameAlreadyExists() {
    var userId = 1;
    var newUsername = "test1";
    var command = new UpdateUserUseCase.UpdateUserCommand(userId, newUsername, null, null);
    when(getUserByIdPort.getUserById(userId)).thenReturn(Optional.of(new User(userId, "test")));
    when(userWithNameExistsPort.userWithNameExists(newUsername)).thenReturn(true);

    assertThatExceptionOfType(UsernameAlreadyInUseException.class)
        .isThrownBy(() -> updateUserService.updateUser(command));
  }

  @Test
  void shouldNotChangePasswordIfPasswordWasNotChanged()
      throws UsernameAlreadyInUseException, InvalidLoginCredentialsException {
    var userId = 1;
    var username = "test";
    var command = new UpdateUserUseCase.UpdateUserCommand(userId, username, null, null);
    when(getUserByIdPort.getUserById(userId)).thenReturn(Optional.of(new User(userId, username)));
    doNothing().when(updateUserPort).updateUser(userId, username, null);

    updateUserService.updateUser(command);

    verify(updateUserPort).updateUser(userId, username, null);
  }

  @Test
  void shouldNotCallUserWithNameExistsIfUsernameIsTheSame()
      throws UsernameAlreadyInUseException, InvalidLoginCredentialsException {
    var userId = 1;
    var username = "test";
    var command = new UpdateUserUseCase.UpdateUserCommand(userId, username, null, null);
    when(getUserByIdPort.getUserById(userId)).thenReturn(Optional.of(new User(userId, username)));
    doNothing().when(updateUserPort).updateUser(userId, username, null);

    updateUserService.updateUser(command);

    verifyNoInteractions(userWithNameExistsPort);
  }

  @Test
  void shouldCallHashIfPasswordWasChanged()
      throws UsernameAlreadyInUseException, InvalidLoginCredentialsException {
    var userId = 1;
    var username = "test";
    var currentPassword = "t3st";
    var currentPasswordHash = "t35t";
    var newPassword = "t3s7";
    var newPasswordHash = "t357";
    var command =
        new UpdateUserUseCase.UpdateUserCommand(userId, username, currentPassword, newPassword);
    when(getUserByIdPort.getUserById(userId)).thenReturn(Optional.of(new User(userId, username)));
    doNothing().when(updateUserPort).updateUser(userId, username, newPasswordHash);
    when(passwordMatchesPort.passwordMatches(userId, currentPasswordHash)).thenReturn(true);
    when(passwordHasher.hash(anyString()))
        .thenAnswer(
            invocation -> {
              var password = (String) invocation.getArgument(0);
              if (password.equals(currentPassword)) {
                return currentPasswordHash;
              }
              if (password.equals(newPassword)) {
                return newPasswordHash;
              }
              throw new InvalidUseOfMatchersException(
                  String.format("Argument %s does not match", password));
            });
    updateUserService.updateUser(command);

    verify(passwordHasher).hash(newPassword);
  }

  @Test
  void shouldUpdateUserIfUserWasChanged()
      throws UsernameAlreadyInUseException, InvalidLoginCredentialsException {
    var userId = 1;
    var newUsername = "test1";
    var username = "test";
    var currentPassword = "t3st";
    var currentPasswordHash = "t35t";
    var newPassword = "t3s7";
    var newPasswordHash = "t357";
    var command =
        new UpdateUserUseCase.UpdateUserCommand(userId, newUsername, currentPassword, newPassword);
    when(userWithNameExistsPort.userWithNameExists(newUsername)).thenReturn(false);
    when(getUserByIdPort.getUserById(userId)).thenReturn(Optional.of(new User(userId, username)));
    doNothing().when(updateUserPort).updateUser(userId, newUsername, newPasswordHash);
    when(passwordMatchesPort.passwordMatches(userId, currentPasswordHash)).thenReturn(true);
    when(passwordHasher.hash(currentPassword)).thenReturn(currentPasswordHash);
    when(passwordHasher.hash(newPassword)).thenReturn(newPasswordHash);

    updateUserService.updateUser(command);

    verify(updateUserPort).updateUser(userId, newUsername, newPasswordHash);
  }

  @Test
  void shouldThrowInvalidLoginCredentialsExceptionIfNewPasswordIsNotNullAndCurrentPasswordIsNull() {
    var userId = 1;
    var username = "test";
    var command = new UpdateUserUseCase.UpdateUserCommand(userId, username, null, "t3s7");
    when(getUserByIdPort.getUserById(userId)).thenReturn(Optional.of(new User(userId, username)));

    assertThatExceptionOfType(InvalidLoginCredentialsException.class)
        .isThrownBy(() -> updateUserService.updateUser(command));
  }

  @Test
  void shouldThrowInvalidLoginCredentialsExceptionIfCurrentPasswordIsIncorrect() {
    var userId = 1;
    var username = "test";
    var currentPassword = "t3st";
    var currentPasswordHash = "t37t";
    var command =
        new UpdateUserUseCase.UpdateUserCommand(userId, username, currentPassword, "t3s7");
    when(getUserByIdPort.getUserById(userId)).thenReturn(Optional.of(new User(userId, username)));
    when(passwordHasher.hash(currentPassword)).thenReturn(currentPasswordHash);
    when(passwordMatchesPort.passwordMatches(userId, currentPasswordHash)).thenReturn(false);

    assertThatExceptionOfType(InvalidLoginCredentialsException.class)
        .isThrownBy(() -> updateUserService.updateUser(command));
  }
}
