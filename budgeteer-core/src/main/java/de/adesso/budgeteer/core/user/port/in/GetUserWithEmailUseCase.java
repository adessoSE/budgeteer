package de.adesso.budgeteer.core.user.port.in;

import de.adesso.budgeteer.core.user.domain.UserWithEmail;

public interface GetUserWithEmailUseCase {
    UserWithEmail getUserWithEmail(long userId);
}
