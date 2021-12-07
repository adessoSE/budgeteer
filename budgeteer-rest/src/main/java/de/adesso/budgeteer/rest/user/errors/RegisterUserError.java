package de.adesso.budgeteer.rest.user.errors;

import lombok.Data;

@Data
public class RegisterUserError {
    private boolean UsernameAlreadyInUse;
    private boolean MailAlreadyInUse;
}
