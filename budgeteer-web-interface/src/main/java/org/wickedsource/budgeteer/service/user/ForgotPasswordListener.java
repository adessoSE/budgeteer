package org.wickedsource.budgeteer.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.persistence.user.UserEntity;

import java.util.UUID;

@Component
public class ForgotPasswordListener implements ApplicationListener<OnForgotPasswordEvent> {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void onApplicationEvent(OnForgotPasswordEvent event) {
        UserEntity userEntity = event.getUserEntity();
        String token = UUID.randomUUID().toString();
        userService.createForgotPasswordTokenForUser(userEntity, token);

        SimpleMailMessage mail = constructMailMessage(event, userEntity, token);
        javaMailSender.send(mail);
    }

    private SimpleMailMessage constructMailMessage(OnForgotPasswordEvent event, UserEntity userEntity, String token) {
        String userMail = userEntity.getMail();
        String subject = "Reset password";
        String confirmationUrl = "http://localhost:8080" + "/resetpassword?resettoken=" + token;
        String message = "Reset password: " + confirmationUrl;
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(userMail);
        mail.setSubject(subject);
        mail.setText(message);
        mail.setFrom("noreply@budgeteer.local");
        return mail;
    }
}
