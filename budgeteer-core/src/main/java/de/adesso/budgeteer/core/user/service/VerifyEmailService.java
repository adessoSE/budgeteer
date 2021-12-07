package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.ExpiredVerificationTokenException;
import de.adesso.budgeteer.core.user.InvalidVerificationTokenException;
import de.adesso.budgeteer.core.user.port.in.VerifyEmailUseCase;
import de.adesso.budgeteer.core.user.port.out.GetVerificationTokenExpirationDatePort;
import de.adesso.budgeteer.core.user.port.out.VerificationTokenExistsPort;
import de.adesso.budgeteer.core.user.port.out.VerifyEmailPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerifyEmailService implements VerifyEmailUseCase {

    private final VerificationTokenExistsPort verificationTokenExistsPort;
    private final GetVerificationTokenExpirationDatePort getVerificationTokenExpirationDatePort;
    private final VerifyEmailPort verifyEmailPort;

    @Override
    public void verifyEmail(String token) throws InvalidVerificationTokenException, ExpiredVerificationTokenException {
        if (!verificationTokenExistsPort.verificationTokenExists(token)) {
            throw new InvalidVerificationTokenException();
        }
        if (getVerificationTokenExpirationDatePort.getVerificationTokenExpirationDate(token).isBefore(LocalDateTime.now())) {
            throw new ExpiredVerificationTokenException();
        }
        verifyEmailPort.verifyEmail(token);
    }
}
