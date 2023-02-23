package de.adesso.budgeteer.core.user.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

import de.adesso.budgeteer.core.user.InvalidLoginCredentialsException;
import de.adesso.budgeteer.core.user.MailAlreadyInUseException;
import de.adesso.budgeteer.core.user.OnEmailChangedEvent;
import de.adesso.budgeteer.core.user.UsernameAlreadyInUseException;
import de.adesso.budgeteer.core.user.domain.UserWithEmail;
import de.adesso.budgeteer.core.user.port.in.UpdateUserUseCase;
import de.adesso.budgeteer.core.user.port.out.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class UpdateUserServiceTest {

  @InjectMocks private UpdateUserService updateUserService;
  @Mock private UserWithNameExistsPort userWithNameExistsPort;
  @Mock private UserWithEmailExistsPort userWithEmailExistsPort;
  @Mock private GetUserWithEmailPort getUserWithEmailPort;
  @Mock private PasswordMatchesPort passwordMatchesPort;
  @Mock private ApplicationEventPublisher eventPublisher;
  @Mock private UpdateUserPort updateUserPort;

  @Test
  void shouldThrowNullPointerExceptionIfUsernameIsNull() {
    var command = new UpdateUserUseCase.UpdateUserCommand(1, null, "test@mail", null, null);
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> updateUserService.updateUser(command));
  }

  @Test
  void shouldThrowNullPointerExceptionIfEmailIsNull() {
    var command = new UpdateUserUseCase.UpdateUserCommand(1, "test", null, null, null);
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> updateUserService.updateUser(command));
  }

  @Test
  void shouldThrowUsernameAlreadyInUseExceptionIfNewUsernameAlreadyExists() {
    var userId = 1;
    var email = "test@mail";
    var newUsername = "test1";
    var command = new UpdateUserUseCase.UpdateUserCommand(userId, newUsername, email, null, null);
    when(getUserWithEmailPort.getUserWithEmail(userId))
        .thenReturn(new UserWithEmail(userId, "test", email));
    when(userWithNameExistsPort.userWithNameExists(newUsername)).thenReturn(true);

    assertThatExceptionOfType(UsernameAlreadyInUseException.class)
        .isThrownBy(() -> updateUserService.updateUser(command));
  }

  @Test
  void shouldThrowMailAlreadyInUseExceptionIfNewEmailAlreadyExists() {
    var userId = 1;
    var username = "test";
    var newEmail = "test1@mail";
    var command = new UpdateUserUseCase.UpdateUserCommand(userId, username, newEmail, null, null);
    when(getUserWithEmailPort.getUserWithEmail(userId))
        .thenReturn(new UserWithEmail(userId, username, "test@mail"));
    when(userWithEmailExistsPort.userWithEmailExists(newEmail)).thenReturn(true);

    assertThatExceptionOfType(MailAlreadyInUseException.class)
        .isThrownBy(() -> updateUserService.updateUser(command));
  }

  @Test
  void shouldNotChangePasswordIfPasswordWasNotChanged()
      throws UsernameAlreadyInUseException, InvalidLoginCredentialsException,
          MailAlreadyInUseException {
    var userId = 1;
    var username = "test";
    var email = "test@mail";
    var command = new UpdateUserUseCase.UpdateUserCommand(userId, username, email, null, null);
    when(getUserWithEmailPort.getUserWithEmail(userId))
        .thenReturn(new UserWithEmail(userId, username, email));
    doNothing().when(updateUserPort).updateUser(userId, username, email, null);

    updateUserService.updateUser(command);

    verify(updateUserPort).updateUser(userId, username, email, null);
  }

  @Test
  void shouldNotCallUserWithNameExistsIfUsernameIsTheSame()
      throws UsernameAlreadyInUseException, InvalidLoginCredentialsException,
          MailAlreadyInUseException {
    var userId = 1;
    var username = "test";
    var email = "test@mail";
    var command = new UpdateUserUseCase.UpdateUserCommand(userId, username, email, null, null);
    when(getUserWithEmailPort.getUserWithEmail(userId))
        .thenReturn(new UserWithEmail(userId, username, email));
    doNothing().when(updateUserPort).updateUser(userId, username, email, null);

    updateUserService.updateUser(command);

    verifyNoInteractions(userWithNameExistsPort);
  }

  @Test
  void shouldNotCallUserWithEmailExistsIfEmailIsTheSame()
      throws UsernameAlreadyInUseException, InvalidLoginCredentialsException,
          MailAlreadyInUseException {
    var userId = 1;
    var username = "test";
    var email = "test@mail";
    var command = new UpdateUserUseCase.UpdateUserCommand(userId, username, email, null, null);
    when(getUserWithEmailPort.getUserWithEmail(userId))
        .thenReturn(new UserWithEmail(userId, username, email));
    doNothing().when(updateUserPort).updateUser(userId, username, email, null);

    updateUserService.updateUser(command);

    verifyNoInteractions(userWithEmailExistsPort);
  }

  @Test
  void shouldUpdateUserIfUserWasChanged()
      throws UsernameAlreadyInUseException, InvalidLoginCredentialsException,
          MailAlreadyInUseException {
    var userId = 1;
    var newUsername = "test1";
    var username = "test";
    var email = "test@mail";
    var newEmail = "test1@mail";
    var currentPassword = "t3st";
    var newPassword = "t3s7";
    var command =
        new UpdateUserUseCase.UpdateUserCommand(
            userId, newUsername, newEmail, currentPassword, newPassword);
    when(userWithNameExistsPort.userWithNameExists(newUsername)).thenReturn(false);
    when(userWithEmailExistsPort.userWithEmailExists(newEmail)).thenReturn(false);
    when(getUserWithEmailPort.getUserWithEmail(userId))
        .thenReturn(new UserWithEmail(userId, username, email));
    doNothing().when(updateUserPort).updateUser(userId, newUsername, newEmail, newPassword);
    doNothing().when(eventPublisher).publishEvent(any());
    when(passwordMatchesPort.passwordMatches(userId, currentPassword)).thenReturn(true);

    updateUserService.updateUser(command);

    verify(updateUserPort).updateUser(userId, newUsername, newEmail, newPassword);
  }

  @Test
  void shouldPublishOnEmailChangedEventIfEmailWasChanged()
      throws UsernameAlreadyInUseException, InvalidLoginCredentialsException,
          MailAlreadyInUseException {
    var userId = 1;
    var username = "test";
    var email = "test@mail";
    var newEmail = "test1@mail";
    var command = new UpdateUserUseCase.UpdateUserCommand(userId, username, newEmail, null, null);
    when(userWithEmailExistsPort.userWithEmailExists(newEmail)).thenReturn(false);
    when(getUserWithEmailPort.getUserWithEmail(userId))
        .thenReturn(new UserWithEmail(userId, username, email));
    doNothing().when(updateUserPort).updateUser(userId, username, newEmail, null);
    doNothing()
        .when(eventPublisher)
        .publishEvent(new OnEmailChangedEvent(userId, username, newEmail));

    updateUserService.updateUser(command);

    verify(eventPublisher).publishEvent(new OnEmailChangedEvent(userId, username, newEmail));
  }

  @Test
  void shouldNotPublishOnEmailChangedEventIfEmailWasNotChanged()
      throws UsernameAlreadyInUseException, InvalidLoginCredentialsException,
          MailAlreadyInUseException {
    var userId = 1;
    var username = "test";
    var email = "test@mail";
    var command = new UpdateUserUseCase.UpdateUserCommand(userId, username, email, null, null);
    when(getUserWithEmailPort.getUserWithEmail(userId))
        .thenReturn(new UserWithEmail(userId, username, email));
    doNothing().when(updateUserPort).updateUser(userId, username, email, null);

    updateUserService.updateUser(command);

    verifyNoInteractions(eventPublisher);
  }

  @Test
  void shouldThrowInvalidLoginCredentialsExceptionIfNewPasswordIsNotNullAndCurrentPasswordIsNull() {
    var userId = 1;
    var username = "test";
    var email = "test@mail";
    var command = new UpdateUserUseCase.UpdateUserCommand(userId, username, email, null, "t3s7");
    when(getUserWithEmailPort.getUserWithEmail(userId))
        .thenReturn(new UserWithEmail(userId, username, email));

    assertThatExceptionOfType(InvalidLoginCredentialsException.class)
        .isThrownBy(() -> updateUserService.updateUser(command));
  }

  @Test
  void shouldThrowInvalidLoginCredentialsExceptionIfCurrentPasswordIsIncorrect() {
    var userId = 1;
    var username = "test";
    var email = "test@mail";
    var currentPassword = "t3st";
    var command =
        new UpdateUserUseCase.UpdateUserCommand(userId, username, email, currentPassword, "t3s7");
    when(getUserWithEmailPort.getUserWithEmail(userId))
        .thenReturn(new UserWithEmail(userId, username, email));
    when(passwordMatchesPort.passwordMatches(userId, currentPassword)).thenReturn(false);

    assertThatExceptionOfType(InvalidLoginCredentialsException.class)
        .isThrownBy(() -> updateUserService.updateUser(command));
  }
}
