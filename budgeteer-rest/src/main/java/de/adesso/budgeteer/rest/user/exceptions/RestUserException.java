package de.adesso.budgeteer.rest.user.exceptions;

import de.adesso.budgeteer.core.user.UserException;

public abstract class RestUserException extends RuntimeException {
    private final UserException userException;

    protected RestUserException(UserException userException) {
        super(userException);
        this.userException = userException;
    }

    public UserException getUserException() {
        return userException;
    }
}
