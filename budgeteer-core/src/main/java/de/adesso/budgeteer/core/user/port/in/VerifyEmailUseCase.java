package de.adesso.budgeteer.core.user.port.in;

import de.adesso.budgeteer.core.user.ExpiredVerificationTokenException;
import de.adesso.budgeteer.core.user.InvalidVerificationTokenException;

public interface VerifyEmailUseCase {
    void verifyEmail(String token) throws InvalidVerificationTokenException, ExpiredVerificationTokenException;
}
