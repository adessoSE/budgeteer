package de.adesso.budgeteer.core.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.core.user.port.out.GetUserByIdPort;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetUserByIdServiceTest {
  @Mock private GetUserByIdPort getUserByIdPort;
  @InjectMocks private GetUserByIdService getUserByIdService;

  @Test
  void shouldReturnUser() {
    var expected = new User(1L, "name");
    when(getUserByIdPort.getUserById(expected.getId())).thenReturn(Optional.of(expected));

    var returned = getUserByIdService.getUserById(expected.getId());

    assertThat(returned).contains(expected);
  }

  @Test
  void shouldReturnEmptyOptionalIfUserDoesNotExist() {
    var id = 1L;
    when(getUserByIdPort.getUserById(id)).thenReturn(Optional.empty());

    var returned = getUserByIdService.getUserById(id);

    assertThat(returned).isEmpty();
  }
}
