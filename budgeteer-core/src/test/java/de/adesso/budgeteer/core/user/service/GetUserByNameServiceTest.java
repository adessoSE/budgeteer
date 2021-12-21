package de.adesso.budgeteer.core.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.core.user.port.out.GetUserByNamePort;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetUserByNameServiceTest {
  @Mock private GetUserByNamePort getUserByNamePort;
  @InjectMocks private GetUserByNameService getUserByNameService;

  @Test
  void shouldReturnUser() {
    var expected = new User(1L, "name");
    when(getUserByNamePort.getUserByName(expected.getName())).thenReturn(Optional.of(expected));

    var returned = getUserByNameService.getUserByName(expected.getName());

    assertThat(returned).contains(expected);
  }

  @Test
  void shouldReturnEmptyOptionalIfUserDoesNotExist() {
    var name = "name";
    when(getUserByNamePort.getUserByName(name)).thenReturn(Optional.empty());

    var returned = getUserByNameService.getUserByName(name);

    assertThat(returned).isEmpty();
  }
}
