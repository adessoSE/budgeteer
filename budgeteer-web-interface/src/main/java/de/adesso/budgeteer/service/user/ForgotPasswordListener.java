package de.adesso.budgeteer.service.user;

import de.adesso.budgeteer.persistence.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ForgotPasswordListener implements ApplicationListener<OnForgotPasswordEvent> {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Autowired(required = false)
    private JavaMailSender javaMailSender;

    /**
     * Sends a mail with a link to reset the password as soon as a user requests a new one via the corresponding page.
     * A random token is generated via the UUID, see: https://www.baeldung.com/java-uuid
     *
     * @param event triggers the corresponding event
     */
    @Override
    public void onApplicationEvent(OnForgotPasswordEvent event) {
        UserEntity userEntity = event.getUserEntity();
        String token = UUID.randomUUID().toString();
        userService.createForgotPasswordTokenForUser(userEntity, token);

        SimpleMailMessage mail = constructMailMessage(event, userEntity, token);
        javaMailSender.send(mail);
    }

    /**
     * Creates a mail with a link for the user to reset his password.
     *
     * @param event the corresponding event
     * @param userEntity the user with the corresponding mail address
     * @param token the generated token
     * @return a SimpleMailMessage with a link to reset the password
     */
    private SimpleMailMessage constructMailMessage(OnForgotPasswordEvent event, UserEntity userEntity, String token) {
        String userMail = userEntity.getMail();
        String subject = "[Budgeteer] Reset password";
        String confirmationUrl = "http://localhost:8080" + "/resetpassword?resettoken=" + token;
        String message = "Hello " + userEntity.getName() + ",\n" +
                "\n" +
                "via this link, which is available for 24 hours, you can reset your password and choose a new one.\n" +
                "\n" +
                confirmationUrl;
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(userMail);
        mail.setSubject(subject);
        mail.setText(message);
        mail.setFrom("noreply@budgeteer.local");
        return mail;
    }
}
