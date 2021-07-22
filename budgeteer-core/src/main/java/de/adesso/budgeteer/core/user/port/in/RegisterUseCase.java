package de.adesso.budgeteer.core.user.port.in;

import de.adesso.budgeteer.core.user.MailAlreadyInUseException;
import de.adesso.budgeteer.core.user.UsernameAlreadyInUseException;
import lombok.Value;

public interface RegisterUseCase {
    void register(RegisterCommand command) throws UsernameAlreadyInUseException, MailAlreadyInUseException;

    @Value
    class RegisterCommand {
        String username;
        String mail;
        String password;
    }
}
