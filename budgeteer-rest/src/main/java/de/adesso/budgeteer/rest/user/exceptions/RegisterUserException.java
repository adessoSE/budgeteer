package de.adesso.budgeteer.rest.user.exceptions;

import de.adesso.budgeteer.core.user.UserException;

public class RegisterUserException extends RestUserException{
    public RegisterUserException(UserException userException) {
        super(userException);
    }
}
