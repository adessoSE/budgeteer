package de.adesso.budgeteer.core.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import de.adesso.budgeteer.core.user.InvalidLoginCredentialsException;
import de.adesso.budgeteer.core.user.PasswordHasher;
import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.core.user.port.out.GetUserByNameAndPasswordPort;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
  @InjectMocks private LoginService loginService;
  @Mock PasswordHasher passwordHasher;
  @Mock GetUserByNameAndPasswordPort getUserByNameAndPasswordPort;

  @Test
  void shouldThrowInvalidLoginCredentialsExceptionIfCredentialsAreWrong() {
    var username = "test";
    var password = "t3st";
    var hashedPassword = "t35t";
    when(getUserByNameAndPasswordPort.getUserByNameAndPassword(username, hashedPassword))
        .thenReturn(Optional.empty());
    when(passwordHasher.hash(password)).thenReturn(hashedPassword);

    assertThatExceptionOfType(InvalidLoginCredentialsException.class)
        .isThrownBy(() -> loginService.login(username, password));
  }

  @Test
  void shouldReturnUserIfLoginCredentialsAreValid() throws InvalidLoginCredentialsException {
    var username = "test";
    var password = "t3st";
    var hashedPassword = "t35t";
    var userId = 1;
    when(getUserByNameAndPasswordPort.getUserByNameAndPassword(username, hashedPassword))
        .thenReturn(Optional.of(new User(userId, username)));
    when(passwordHasher.hash(password)).thenReturn(hashedPassword);

    var user = loginService.login(username, password);

    assertThat(user).isEqualTo(new User(userId, username));
  }
}
