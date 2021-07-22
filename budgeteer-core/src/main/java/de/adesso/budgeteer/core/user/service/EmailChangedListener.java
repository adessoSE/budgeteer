package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.EmailAlreadyVerifiedException;
import de.adesso.budgeteer.core.user.MailNotEnabledException;
import de.adesso.budgeteer.core.user.OnEmailChangedEvent;
import de.adesso.budgeteer.core.user.port.in.ResendVerificationTokenUseCase;
import de.adesso.budgeteer.core.user.port.out.CreateVerificationTokenPort;
import de.adesso.budgeteer.core.user.port.out.DeleteVerificationTokenByUserIdPort;
import de.adesso.budgeteer.core.user.port.out.EmailVerifiedPort;
import de.adesso.budgeteer.core.user.port.out.GetUserWithEmailPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailChangedListener implements ApplicationListener<OnEmailChangedEvent>, ResendVerificationTokenUseCase {

    private final JavaMailSender javaMailSender;
    private final CreateVerificationTokenPort createVerificationTokenPort;
    private final EmailVerifiedPort emailVerifiedPort;
    private final GetUserWithEmailPort getUserWithEmailPort;

    @Value("${budgeteer.mail.activate}")
    private boolean emailEnabled;

    private static final String SUBJECT = "[Budgeteer] Verify your email address";
    private static final String MESSAGE_TEMPLATE = "Hello %s,\n\n" +
            "please verify your email address via this link, which is available for 24 hours.\n\n" +
            "http://localhost:8080/login?verificationtoken=%s";

    /**
     * Sends a mail with a link to verify the mail address as soon as the user registers.
     * A random token is generated via the UUID, see: https://www.baeldung.com/java-uuid
     *
     * @param event triggers the corresponding event
     */
    @Override
    public void onApplicationEvent(OnEmailChangedEvent event) {
        sendVerificationToken(event.getUserId(), event.getName(), event.getEmail());
    }

    @Override
    public void resendVerificationToken(long userId) throws EmailAlreadyVerifiedException, MailNotEnabledException {
        if (!emailEnabled) {
            throw new MailNotEnabledException();
        }
        var user = getUserWithEmailPort.getUserWithEmail(userId);
        if (emailVerifiedPort.emailVerified(user.getEmail())) {
            throw new EmailAlreadyVerifiedException();
        }
        sendVerificationToken(userId, user.getName(), user.getEmail());
    }

    private void sendVerificationToken(long userId, String name, String email) {
        if (!emailEnabled) {
            return;
        }
        var token = UUID.randomUUID().toString();

        createVerificationTokenPort.createVerificationToken(userId, token, LocalDateTime.now().plusHours(24));

        javaMailSender.send(mimeMessage -> {
            var messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setSubject(SUBJECT);
            messageHelper.setFrom("noreply@budgeteer.local");
            messageHelper.setTo(email);
            messageHelper.setText(String.format(MESSAGE_TEMPLATE, name, token));
        });
    }
}
