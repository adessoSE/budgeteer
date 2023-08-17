package de.adesso.budgeteer.core.user.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

import de.adesso.budgeteer.core.user.UserException;
import de.adesso.budgeteer.core.user.port.in.RegisterUseCase;
import de.adesso.budgeteer.core.user.port.out.CreateUserPort;
import de.adesso.budgeteer.core.user.port.out.UserWithNameExistsPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

  @InjectMocks private RegisterService registerService;
  @Mock private UserWithNameExistsPort userWithNameExistsPort;
  @Mock private CreateUserPort createUserPort;

  @Test
  void shouldThrowUsernameAlreadyInUseExceptionIfUserWithUsernameExists() {
    var command = new RegisterUseCase.RegisterCommand("test", "test");
    when(userWithNameExistsPort.userWithNameExists("test")).thenReturn(true);

    assertThatExceptionOfType(UserException.class)
        .isThrownBy(() -> registerService.register(command))
        .matches(e -> e.getCauses().contains(UserException.UserErrors.USERNAME_ALREADY_IN_USE));
  }

  @Test
  void shouldCallCreateUserWhenUserIsValid() throws UserException {
    var command = new RegisterUseCase.RegisterCommand("test", "t3st");
    when(userWithNameExistsPort.userWithNameExists("test")).thenReturn(false);
    when(createUserPort.createUser(anyString(), anyString())).thenReturn(1L);

    registerService.register(command);

    verify(createUserPort).createUser(command.getUsername(), command.getPassword());
  }
}
