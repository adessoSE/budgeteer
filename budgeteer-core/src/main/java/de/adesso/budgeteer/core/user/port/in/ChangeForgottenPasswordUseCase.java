package de.adesso.budgeteer.core.user.port.in;

import de.adesso.budgeteer.core.user.ExpiredForgottenPasswordTokenException;
import de.adesso.budgeteer.core.user.InvalidForgottenPasswordTokenException;
import lombok.Value;

public interface ChangeForgottenPasswordUseCase {
    void changeForgottenPassword(ChangeForgottenPasswordCommand command) throws InvalidForgottenPasswordTokenException, ExpiredForgottenPasswordTokenException;

    @Value
    class ChangeForgottenPasswordCommand {
        String token;
        String password;
    }
}
