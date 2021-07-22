package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.MailNotEnabledException;
import de.adesso.budgeteer.core.user.MailNotFoundException;
import de.adesso.budgeteer.core.user.MailNotVerifiedException;
import de.adesso.budgeteer.core.user.port.in.GetUserByEmailUseCase;
import de.adesso.budgeteer.core.user.port.in.ResetPasswordUseCase;
import de.adesso.budgeteer.core.user.port.out.CreateForgottenTokenPort;
import de.adesso.budgeteer.core.user.port.out.EmailVerifiedPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResetPasswordService implements ResetPasswordUseCase {

    private final EmailVerifiedPort emailVerifiedPort;
    private final GetUserByEmailUseCase getUserByEmailUseCase;
    private final CreateForgottenTokenPort createForgottenTokenPort;
    private final JavaMailSender mailSender;

    @Value("${budgeteer.mail.activate}")
    private boolean mailActivated;

    private static final String SUBJECT = "[Budgeteer] Reset password";
    private static final String MESSAGE_TEMPLATE = "Hello %s\n\n" +
            "via this link, which is available for 24 hours, you can reset " +
            "your password and choose a new one. \n\n" +
            "http://localhost:8080/resetpassword?resettoken=%s";

    @Override
    public void resetPassword(String mail) throws MailNotFoundException, MailNotVerifiedException, MailNotEnabledException {
        if (!mailActivated) {
            throw new MailNotEnabledException();
        }
        var user = getUserByEmailUseCase.getUserByEmail(mail).orElseThrow(MailNotFoundException::new);
        if (!emailVerifiedPort.emailVerified(mail)) {
            throw new MailNotVerifiedException();
        }
        var token = UUID.randomUUID().toString();
        createForgottenTokenPort.createForgottenPasswordToken(user.getId(), token, LocalDateTime.now().plusHours(24));
        mailSender.send(mimeMessage -> {
            var messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(mail);
            messageHelper.setFrom("noreply@budgeteer.local");
            messageHelper.setSubject(SUBJECT);
            messageHelper.setText(String.format(MESSAGE_TEMPLATE, user.getName(), token));
        });
    }
}
