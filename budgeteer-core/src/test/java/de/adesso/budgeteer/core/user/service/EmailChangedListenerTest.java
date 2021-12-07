package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.EmailAlreadyVerifiedException;
import de.adesso.budgeteer.core.user.MailNotEnabledException;
import de.adesso.budgeteer.core.user.OnEmailChangedEvent;
import de.adesso.budgeteer.core.user.domain.UserWithEmail;
import de.adesso.budgeteer.core.user.port.out.CreateVerificationTokenPort;
import de.adesso.budgeteer.core.user.port.out.DeleteVerificationTokenByUserIdPort;
import de.adesso.budgeteer.core.user.port.out.EmailVerifiedPort;
import de.adesso.budgeteer.core.user.port.out.GetUserWithEmailPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailChangedListenerTest {

    @InjectMocks private EmailChangedListener emailChangedListener;
    @Mock private JavaMailSender javaMailSender;
    @Mock private CreateVerificationTokenPort createVerificationTokenPort;
    @Mock private EmailVerifiedPort emailVerifiedPort;
    @Mock private GetUserWithEmailPort getUserWithEmailPort;

    @Test
    void shouldDoNothingOnEmailChangedEventIfEmailIsNotEnabled() {
        ReflectionTestUtils.setField(emailChangedListener, "emailEnabled", false);

        verifyNoInteractions(createVerificationTokenPort, javaMailSender);
    }

    @Test
    void shouldThrowMailNotEnabledExceptionIfEmailIsNotEnabled() {
        ReflectionTestUtils.setField(emailChangedListener, "emailEnabled", false);

        assertThatExceptionOfType(MailNotEnabledException.class).isThrownBy(() -> emailChangedListener.resendVerificationToken(1));
    }

    @Test
    void shouldThrowEmailAlreadyVerifiedExceptionIfEmailIsAlreadyVerified() {
        ReflectionTestUtils.setField(emailChangedListener, "emailEnabled", true);
        var email = "test@mail";
        var userId = 1L;
        when(getUserWithEmailPort.getUserWithEmail(userId)).thenReturn(new UserWithEmail(userId, "test", email));
        when(emailVerifiedPort.emailVerified(email)).thenReturn(true);

        assertThatExceptionOfType(EmailAlreadyVerifiedException.class).isThrownBy(() -> emailChangedListener.resendVerificationToken(userId));
    }

    @Test
    void shouldCreateVerificationTokenOnEmailChangedEvent() {
        ReflectionTestUtils.setField(emailChangedListener, "emailEnabled", true);
        var userId = 1L;
        var username = "test";
        var email = "test@mail";
        doNothing().when(createVerificationTokenPort).createVerificationToken(anyLong(), anyString(), any());
        doNothing().when(javaMailSender).send(any(MimeMessagePreparator.class));

        emailChangedListener.onApplicationEvent(new OnEmailChangedEvent(userId, username, email));

        verify(createVerificationTokenPort).createVerificationToken(anyLong(), anyString(), any());
    }

    @Test
    void shouldSendEmailOnEmailChangedEvent() {
        ReflectionTestUtils.setField(emailChangedListener, "emailEnabled", true);
        var userId = 1L;
        var username = "test";
        var email = "test@mail";
        doNothing().when(createVerificationTokenPort).createVerificationToken(anyLong(), anyString(), any());
        doNothing().when(javaMailSender).send(any(MimeMessagePreparator.class));

        emailChangedListener.onApplicationEvent(new OnEmailChangedEvent(userId, username, email));

        verify(javaMailSender).send(any(MimeMessagePreparator.class));
    }
}
