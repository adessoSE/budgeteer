package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.ExpiredForgottenPasswordTokenException;
import de.adesso.budgeteer.core.user.InvalidForgottenPasswordTokenException;
import de.adesso.budgeteer.core.user.PasswordHasher;
import de.adesso.budgeteer.core.user.port.in.ChangeForgottenPasswordUseCase;
import de.adesso.budgeteer.core.user.port.out.ChangeForgottenPasswordPort;
import de.adesso.budgeteer.core.user.port.out.ForgottenPasswordTokenExistsPort;
import de.adesso.budgeteer.core.user.port.out.GetForgottenPasswordTokenExpirationDatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChangeForgottenPasswordService implements ChangeForgottenPasswordUseCase {

    private final ChangeForgottenPasswordPort changeForgottenPasswordPort;
    private final ForgottenPasswordTokenExistsPort forgottenPasswordTokenExistsPort;
    private final GetForgottenPasswordTokenExpirationDatePort getForgottenPasswordTokenExpirationDatePort;
    private final PasswordHasher passwordHasher;

    @Override
    public void changeForgottenPassword(ChangeForgottenPasswordCommand command) throws InvalidForgottenPasswordTokenException, ExpiredForgottenPasswordTokenException {
        if (!forgottenPasswordTokenExistsPort.forgottenPasswordTokenExists(command.getToken())) {
            throw new InvalidForgottenPasswordTokenException();
        }
        if (getForgottenPasswordTokenExpirationDatePort.getForgottenPasswordTokenExpirationDate(command.getToken()).isBefore(LocalDateTime.now())) {
            throw new ExpiredForgottenPasswordTokenException();
        }
        changeForgottenPasswordPort.changeForgottenPassword(command.getToken(), passwordHasher.hash(command.getPassword()));
    }
}
