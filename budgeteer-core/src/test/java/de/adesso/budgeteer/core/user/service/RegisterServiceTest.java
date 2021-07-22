package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.MailAlreadyInUseException;
import de.adesso.budgeteer.core.user.OnEmailChangedEvent;
import de.adesso.budgeteer.core.user.PasswordHasher;
import de.adesso.budgeteer.core.user.UsernameAlreadyInUseException;
import de.adesso.budgeteer.core.user.port.in.RegisterUseCase;
import de.adesso.budgeteer.core.user.port.out.CreateUserPort;
import de.adesso.budgeteer.core.user.port.out.UserWithEmailExistsPort;
import de.adesso.budgeteer.core.user.port.out.UserWithNameExistsPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @InjectMocks private RegisterService registerService;
    @Mock private UserWithNameExistsPort userWithNameExistsPort;
    @Mock private UserWithEmailExistsPort userWithEmailExistsPort;
    @Mock private CreateUserPort createUserPort;
    @Mock private PasswordHasher passwordHasher;
    @Mock private ApplicationEventPublisher eventPublisher;

    @Test
    void shouldThrowUsernameAlreadyInUseExceptionIfUserWithUsernameExists() {
        var command = new RegisterUseCase.RegisterCommand("test", "test@mail", "test");
        when(userWithNameExistsPort.userWithNameExists("test")).thenReturn(true);

        assertThatExceptionOfType(UsernameAlreadyInUseException.class)
                .isThrownBy(() -> registerService.register(command));

    }

    @Test
    void shouldThrowMailAlreadyInUseExceptionIfUserWithEmailExists() {
        var command = new RegisterUseCase.RegisterCommand("test", "test@mail", "test");
        when(userWithNameExistsPort.userWithNameExists("test")).thenReturn(false);
        when(userWithEmailExistsPort.userWithEmailExists("test@mail")).thenReturn(true);

        assertThatExceptionOfType(MailAlreadyInUseException.class)
                .isThrownBy(() -> registerService.register(command));
    }

    @Test
    void shouldHashPasswordWhenRegisteringUser() throws UsernameAlreadyInUseException, MailAlreadyInUseException {
        var command = new RegisterUseCase.RegisterCommand("test", "test@mail", "t3st");
        when(userWithNameExistsPort.userWithNameExists("test")).thenReturn(false);
        when(userWithEmailExistsPort.userWithEmailExists("test@mail")).thenReturn(false);
        when(passwordHasher.hash(anyString())).thenReturn("t35t");
        when(createUserPort.createUser(anyString(), anyString(), anyString())).thenReturn(1L);
        doNothing().when(eventPublisher).publishEvent(any());

        registerService.register(command);

        verify(passwordHasher).hash("t3st");
    }

    @Test
    void shouldCallCreateUserWhenUserIsValid() throws UsernameAlreadyInUseException, MailAlreadyInUseException {
        var command = new RegisterUseCase.RegisterCommand("test", "test@mail", "t3st");
        when(userWithNameExistsPort.userWithNameExists("test")).thenReturn(false);
        when(userWithEmailExistsPort.userWithEmailExists("test@mail")).thenReturn(false);
        when(passwordHasher.hash(anyString())).thenReturn("t35t");
        when(createUserPort.createUser(anyString(), anyString(), anyString())).thenReturn(1L);
        doNothing().when(eventPublisher).publishEvent(any());

        registerService.register(command);

        verify(createUserPort).createUser("test", "test@mail", "t35t");
    }

    @Test
    void shouldPublishOnEmailChangedEventWhenUserIsCreated() throws UsernameAlreadyInUseException, MailAlreadyInUseException {
        var command = new RegisterUseCase.RegisterCommand("test", "test@mail", "t3st");
        when(userWithNameExistsPort.userWithNameExists("test")).thenReturn(false);
        when(userWithEmailExistsPort.userWithEmailExists("test@mail")).thenReturn(false);
        when(passwordHasher.hash(anyString())).thenReturn("t35t");
        when(createUserPort.createUser(anyString(), anyString(), anyString())).thenReturn(1L);
        doNothing().when(eventPublisher).publishEvent(any());

        registerService.register(command);

        verify(eventPublisher).publishEvent(new OnEmailChangedEvent(1, "test", "test@mail"));
    }
}
