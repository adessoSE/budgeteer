package de.adesso.budgeteer.core.user.port.in;

import de.adesso.budgeteer.core.user.MailNotEnabledException;
import de.adesso.budgeteer.core.user.MailNotFoundException;
import de.adesso.budgeteer.core.user.MailNotVerifiedException;

public interface ResetPasswordUseCase {
    void resetPassword(String mail) throws MailNotFoundException, MailNotVerifiedException, MailNotEnabledException;
}
