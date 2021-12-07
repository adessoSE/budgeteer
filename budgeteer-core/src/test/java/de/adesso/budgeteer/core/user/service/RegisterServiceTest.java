package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.OnEmailChangedEvent;
import de.adesso.budgeteer.core.user.UserException;
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
    @Mock private ApplicationEventPublisher eventPublisher;

    @Test
    void shouldThrowUsernameAlreadyInUseExceptionIfUserWithUsernameExists() {
        var command = new RegisterUseCase.RegisterCommand("test", "test@mail", "test");
        when(userWithNameExistsPort.userWithNameExists("test")).thenReturn(true);

        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() -> registerService.register(command))
                .matches(e -> e.getCauses().contains(UserException.UserErrors.USERNAME_ALREADY_IN_USE));

    }

    @Test
    void shouldThrowMailAlreadyInUseExceptionIfUserWithEmailExists() {
        var command = new RegisterUseCase.RegisterCommand("test", "test@mail", "test");
        when(userWithNameExistsPort.userWithNameExists("test")).thenReturn(false);
        when(userWithEmailExistsPort.userWithEmailExists("test@mail")).thenReturn(true);

        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() -> registerService.register(command))
                .matches(e -> e.getCauses().contains(UserException.UserErrors.MAIL_ALREADY_IN_USE));
    }

    @Test
    void shouldCallCreateUserWhenUserIsValid() throws UserException {
        var command = new RegisterUseCase.RegisterCommand("test", "test@mail", "t3st");
        when(userWithNameExistsPort.userWithNameExists("test")).thenReturn(false);
        when(userWithEmailExistsPort.userWithEmailExists("test@mail")).thenReturn(false);
        when(createUserPort.createUser(anyString(), anyString(), anyString())).thenReturn(1L);
        doNothing().when(eventPublisher).publishEvent(any());

        registerService.register(command);

        verify(createUserPort).createUser(command.getUsername(), command.getMail(), command.getPassword());
    }

    @Test
    void shouldPublishOnEmailChangedEventWhenUserIsCreated() throws UserException {
        var command = new RegisterUseCase.RegisterCommand("test", "test@mail", "t3st");
        when(userWithNameExistsPort.userWithNameExists("test")).thenReturn(false);
        when(userWithEmailExistsPort.userWithEmailExists("test@mail")).thenReturn(false);
        when(createUserPort.createUser(anyString(), anyString(), anyString())).thenReturn(1L);
        doNothing().when(eventPublisher).publishEvent(any());

        registerService.register(command);

        verify(eventPublisher).publishEvent(new OnEmailChangedEvent(1, "test", "test@mail"));
    }
}
