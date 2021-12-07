package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.ExpiredVerificationTokenException;
import de.adesso.budgeteer.core.user.InvalidVerificationTokenException;
import de.adesso.budgeteer.core.user.port.out.DeleteVerificationTokenByTokenPort;
import de.adesso.budgeteer.core.user.port.out.GetVerificationTokenExpirationDatePort;
import de.adesso.budgeteer.core.user.port.out.VerificationTokenExistsPort;
import de.adesso.budgeteer.core.user.port.out.VerifyEmailPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerifyEmailServiceTest {

    @InjectMocks private VerifyEmailService verifyEmailService;
    @Mock private VerificationTokenExistsPort verificationTokenExistsPort;
    @Mock private GetVerificationTokenExpirationDatePort getVerificationTokenExpirationDatePort;
    @Mock private VerifyEmailPort verifyEmailPort;

    @Test
    void shouldThrowInvalidVerificationTokenExceptionIfVerificationTokenIsInvalid() {
        var token = "token";
        when(verificationTokenExistsPort.verificationTokenExists(token)).thenReturn(false);

        assertThatExceptionOfType(InvalidVerificationTokenException.class).isThrownBy(() -> verifyEmailService.verifyEmail(token));
    }

    @Test
    void shouldThrowExpiredVerificationTokenExceptionIfExpiryDateIsInPast() {
        var token = "token";
        var expiryDate = LocalDateTime.now().minusDays(1);
        when(verificationTokenExistsPort.verificationTokenExists(token)).thenReturn(true);
        when(getVerificationTokenExpirationDatePort.getVerificationTokenExpirationDate(token)).thenReturn(expiryDate);

        assertThatExceptionOfType(ExpiredVerificationTokenException.class).isThrownBy(() -> verifyEmailService.verifyEmail(token));
    }

    @Test
    void shouldCallVerifyEmailIfTokenIsValid() throws ExpiredVerificationTokenException, InvalidVerificationTokenException {
        var token = "token";
        var expiryDate = LocalDateTime.now().plusDays(1);
        when(verificationTokenExistsPort.verificationTokenExists(token)).thenReturn(true);
        when(getVerificationTokenExpirationDatePort.getVerificationTokenExpirationDate(token)).thenReturn(expiryDate);
        doNothing().when(verifyEmailPort).verifyEmail(token);

        verifyEmailService.verifyEmail(token);

        verify(verifyEmailPort).verifyEmail(token);
    }
}
