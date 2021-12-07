package de.adesso.budgeteer.rest.user;

import de.adesso.budgeteer.core.user.UserException;
import de.adesso.budgeteer.rest.user.errors.RegisterUserError;
import de.adesso.budgeteer.rest.user.exceptions.RegisterUserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserControllerAdvice {
    @ExceptionHandler(RegisterUserException.class)
    public ResponseEntity<RegisterUserError> handleRegisterUserException(RegisterUserException registerUserException) {
        var causes = registerUserException.getUserException().getCauses().getAll();

        var registerUserError = new RegisterUserError();
        registerUserError.setUsernameAlreadyInUse(causes.contains(UserException.UserErrors.USERNAME_ALREADY_IN_USE));
        registerUserError.setMailAlreadyInUse(causes.contains(UserException.UserErrors.MAIL_ALREADY_IN_USE));

        return ResponseEntity.badRequest().body(registerUserError);
    }
}
