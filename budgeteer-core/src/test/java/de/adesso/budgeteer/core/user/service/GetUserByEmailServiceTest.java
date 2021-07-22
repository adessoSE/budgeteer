package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.core.user.port.out.GetUserByEmailPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserByEmailServiceTest {

    @InjectMocks private GetUserByEmailService getUserByEmailService;
    @Mock private GetUserByEmailPort getUserByEmailPort;

    @Test
    void shouldReturnOptionalOfUserIfUserExists() {
        var email = "test@mail";
        var expectedUser = Optional.of(new User(1, "test"));
        when(getUserByEmailPort.getUserByEmail(email)).thenReturn(expectedUser);

        var returnedUser = getUserByEmailService.getUserByEmail(email);

        assertThat(returnedUser).isEqualTo(expectedUser);
    }

    @Test
    void shouldReturnEmptyOptionalIfUserDoesNotExists() {
        var email = "test@mail";
        var expectedUser = Optional.<User>empty();
        when(getUserByEmailPort.getUserByEmail(email)).thenReturn(expectedUser);

        var returnedUser = getUserByEmailService.getUserByEmail(email);

        assertThat(returnedUser).isEqualTo(expectedUser);
    }
}
