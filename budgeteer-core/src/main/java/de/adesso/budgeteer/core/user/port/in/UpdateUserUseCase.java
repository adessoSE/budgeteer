package de.adesso.budgeteer.core.user.port.in;

import de.adesso.budgeteer.core.user.InvalidLoginCredentialsException;
import de.adesso.budgeteer.core.user.MailAlreadyInUseException;
import de.adesso.budgeteer.core.user.UsernameAlreadyInUseException;
import lombok.Value;

public interface UpdateUserUseCase {
    void updateUser(UpdateUserCommand command) throws UsernameAlreadyInUseException, MailAlreadyInUseException, InvalidLoginCredentialsException;

    @Value
    class UpdateUserCommand {
        long id;
        String name;
        String email;
        String currentPassword;
        String newPassword;
    }
}
