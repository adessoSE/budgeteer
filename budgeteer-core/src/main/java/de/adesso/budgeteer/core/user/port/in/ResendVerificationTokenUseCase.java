package de.adesso.budgeteer.core.user.port.in;

import de.adesso.budgeteer.core.user.EmailAlreadyVerifiedException;
import de.adesso.budgeteer.core.user.MailNotEnabledException;

public interface ResendVerificationTokenUseCase {
    void resendVerificationToken(long userId) throws EmailAlreadyVerifiedException, MailNotEnabledException;
}
