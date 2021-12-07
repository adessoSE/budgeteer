package de.adesso.budgeteer.core.user;

import de.adesso.budgeteer.core.common.Causes;
import lombok.Getter;

@Getter
public class UserException extends Exception {
    private final Causes<UserErrors> causes;

    public UserException(Causes<UserErrors> causes) {
        this.causes = causes;
    }

    public enum UserErrors {
        USERNAME_ALREADY_IN_USE,
        MAIL_ALREADY_IN_USE
    }
}
