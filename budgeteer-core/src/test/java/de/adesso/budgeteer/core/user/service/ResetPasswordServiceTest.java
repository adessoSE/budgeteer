package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.MailNotEnabledException;
import de.adesso.budgeteer.core.user.MailNotFoundException;
import de.adesso.budgeteer.core.user.MailNotVerifiedException;
import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.core.user.port.in.GetUserByEmailUseCase;
import de.adesso.budgeteer.core.user.port.out.CreateForgottenTokenPort;
import de.adesso.budgeteer.core.user.port.out.EmailVerifiedPort;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResetPasswordServiceTest {

    @InjectMocks private ResetPasswordService resetPasswordService;
    @Mock private EmailVerifiedPort emailVerifiedPort;
    @Mock private GetUserByEmailUseCase getUserByEmailUseCase;
    @Mock private CreateForgottenTokenPort createForgottenTokenPort;
    @Mock private JavaMailSender mailSender;

    @Test
    void shouldThrowMailNotEnabledExceptionIfEmailIsNotEnabled() {
        ReflectionTestUtils.setField(resetPasswordService, "mailActivated", false);

        AssertionsForClassTypes.assertThatExceptionOfType(MailNotEnabledException.class).isThrownBy(() -> resetPasswordService.resetPassword("test@mail"));
    }

    @Test
    void shouldThrowMailNotFoundExceptionIfEmailDoesNotExist() {
        var email = "test@mail";
        ReflectionTestUtils.setField(resetPasswordService, "mailActivated", true);
        when(getUserByEmailUseCase.getUserByEmail(email)).thenReturn(Optional.empty());

        AssertionsForClassTypes.assertThatExceptionOfType(MailNotFoundException.class).isThrownBy(() -> resetPasswordService.resetPassword(email));
    }

    @Test
    void shouldThrowMailNotVerifiedExceptionIfEmailIsNotVerified() {
        var email = "test@mail";
        ReflectionTestUtils.setField(resetPasswordService, "mailActivated", true);
        when(getUserByEmailUseCase.getUserByEmail(email)).thenReturn(Optional.of(new User(1, "test")));
        when(emailVerifiedPort.emailVerified(email)).thenReturn(false);

        AssertionsForClassTypes.assertThatExceptionOfType(MailNotVerifiedException.class).isThrownBy(() -> resetPasswordService.resetPassword(email));
    }

    @Test
    void shouldCreateForgottenPasswordTokenIfEmailIsValid() throws MailNotFoundException, MailNotEnabledException, MailNotVerifiedException {
        var email = "test@mail";
        var userId = 1;
        ReflectionTestUtils.setField(resetPasswordService, "mailActivated", true);
        when(getUserByEmailUseCase.getUserByEmail(email)).thenReturn(Optional.of(new User(userId, "test")));
        when(emailVerifiedPort.emailVerified(email)).thenReturn(true);
        doNothing().when(createForgottenTokenPort).createForgottenPasswordToken(anyLong(), anyString(), any());
        doNothing().when(mailSender).send(any(MimeMessagePreparator.class));

        resetPasswordService.resetPassword(email);

        verify(createForgottenTokenPort).createForgottenPasswordToken(anyLong(), anyString(), any());
    }

    @Test
    void shouldSendEmailWhenEmailIsValid() throws MailNotFoundException, MailNotEnabledException, MailNotVerifiedException {
        var email = "test@mail";
        var userId = 1;
        ReflectionTestUtils.setField(resetPasswordService, "mailActivated", true);
        when(getUserByEmailUseCase.getUserByEmail(email)).thenReturn(Optional.of(new User(userId, "test")));
        when(emailVerifiedPort.emailVerified(email)).thenReturn(true);
        doNothing().when(createForgottenTokenPort).createForgottenPasswordToken(anyLong(), anyString(), any());
        doNothing().when(mailSender).send(any(MimeMessagePreparator.class));

        resetPasswordService.resetPassword(email);

        verify(mailSender).send(any(MimeMessagePreparator.class));
    }
}
