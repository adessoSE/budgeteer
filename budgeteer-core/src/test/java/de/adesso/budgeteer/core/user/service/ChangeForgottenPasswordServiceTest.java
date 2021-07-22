package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.ExpiredForgottenPasswordTokenException;
import de.adesso.budgeteer.core.user.InvalidForgottenPasswordTokenException;
import de.adesso.budgeteer.core.user.PasswordHasher;
import de.adesso.budgeteer.core.user.port.in.ChangeForgottenPasswordUseCase;
import de.adesso.budgeteer.core.user.port.out.ChangeForgottenPasswordPort;
import de.adesso.budgeteer.core.user.port.out.ForgottenPasswordTokenExistsPort;
import de.adesso.budgeteer.core.user.port.out.GetForgottenPasswordTokenExpirationDatePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangeForgottenPasswordServiceTest {

    @InjectMocks
    private ChangeForgottenPasswordService changeForgottenPasswordService;
    @Mock
    private ChangeForgottenPasswordPort changeForgottenPasswordPort;
    @Mock
    private ForgottenPasswordTokenExistsPort forgottenPasswordTokenExistsPort;
    @Mock
    private GetForgottenPasswordTokenExpirationDatePort getForgottenPasswordTokenExpirationDatePort;
    @Mock
    private PasswordHasher passwordHasher;

    @Test
    void shouldThrowInvalidForgottenPasswordTokenExceptionIfTokenDoesNotExist() {
        var token = "token";
        var command = new ChangeForgottenPasswordUseCase.ChangeForgottenPasswordCommand(token, "t3st");
        when(forgottenPasswordTokenExistsPort.forgottenPasswordTokenExists(token)).thenReturn(false);

        assertThatExceptionOfType(InvalidForgottenPasswordTokenException.class)
                .isThrownBy(() -> changeForgottenPasswordService.changeForgottenPassword(command));
    }

    @Test
    void shouldThrowExpiredForgottenPasswordTokenExceptionIfTokenIsExpired() {
        var token = "token";
        var expiryDate = LocalDateTime.now().minusDays(1);
        var command = new ChangeForgottenPasswordUseCase.ChangeForgottenPasswordCommand(token, "t3st");
        when(forgottenPasswordTokenExistsPort.forgottenPasswordTokenExists(token)).thenReturn(true);
        when(getForgottenPasswordTokenExpirationDatePort.getForgottenPasswordTokenExpirationDate(token)).thenReturn(expiryDate);

        assertThatExceptionOfType(ExpiredForgottenPasswordTokenException.class)
                .isThrownBy(() -> changeForgottenPasswordService.changeForgottenPassword(command));
    }

    @Test
    void shouldHashPasswordIfTokenIsValid() throws InvalidForgottenPasswordTokenException, ExpiredForgottenPasswordTokenException {
        var token = "token";
        var expiryDate = LocalDateTime.now().plusDays(1);
        var newPassword = "t3st";
        var newPasswordHash = "t37t";
        var command = new ChangeForgottenPasswordUseCase.ChangeForgottenPasswordCommand(token, newPassword);
        when(forgottenPasswordTokenExistsPort.forgottenPasswordTokenExists(token)).thenReturn(true);
        when(getForgottenPasswordTokenExpirationDatePort.getForgottenPasswordTokenExpirationDate(token)).thenReturn(expiryDate);
        when(passwordHasher.hash(newPassword)).thenReturn(newPasswordHash);
        doNothing().when(changeForgottenPasswordPort).changeForgottenPassword(token, newPasswordHash);

        changeForgottenPasswordService.changeForgottenPassword(command);

        verify(passwordHasher).hash(newPassword);
    }

    @Test
    void shouldCallChangeForgottenPasswordIfTokenIsValid() throws InvalidForgottenPasswordTokenException, ExpiredForgottenPasswordTokenException {
        var token = "token";
        var expiryDate = LocalDateTime.now().plusDays(1);
        var newPassword = "t3st";
        var newPasswordHash = "t37t";
        var command = new ChangeForgottenPasswordUseCase.ChangeForgottenPasswordCommand(token, newPassword);
        when(forgottenPasswordTokenExistsPort.forgottenPasswordTokenExists(token)).thenReturn(true);
        when(getForgottenPasswordTokenExpirationDatePort.getForgottenPasswordTokenExpirationDate(token)).thenReturn(expiryDate);
        when(passwordHasher.hash(newPassword)).thenReturn(newPasswordHash);
        doNothing().when(changeForgottenPasswordPort).changeForgottenPassword(token, newPasswordHash);

        changeForgottenPasswordService.changeForgottenPassword(command);

        verify(changeForgottenPasswordPort).changeForgottenPassword(token, newPasswordHash);
    }
}
