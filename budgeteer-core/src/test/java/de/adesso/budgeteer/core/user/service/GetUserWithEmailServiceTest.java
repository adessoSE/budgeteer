package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.domain.UserWithEmail;
import de.adesso.budgeteer.core.user.port.out.GetUserWithEmailPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserWithEmailServiceTest {

    @InjectMocks private GetUserWithEmailService getUserWithEmailService;
    @Mock private GetUserWithEmailPort getUserWithEmailPort;

    @Test
    void shouldReturnUserWithEmailIfUserExists() {
        var userId = 1543L;
        var expectedUser = new UserWithEmail(userId, "test", "test@mail");
        when(getUserWithEmailPort.getUserWithEmail(userId)).thenReturn(expectedUser);

        var returnedUser = getUserWithEmailService.getUserWithEmail(userId);

        assertThat(returnedUser).isEqualTo(expectedUser);
    }

    @Test
    void shouldReturnNullIfUserDoesNotExist() {
        var userId = 1545L;
        when(getUserWithEmailPort.getUserWithEmail(userId)).thenReturn(null);

        var returnedUser = getUserWithEmailService.getUserWithEmail(userId);

        assertThat(returnedUser).isNull();
    }
}
