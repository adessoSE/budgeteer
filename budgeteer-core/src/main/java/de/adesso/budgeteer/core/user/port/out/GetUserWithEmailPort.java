package de.adesso.budgeteer.core.user.port.out;

import de.adesso.budgeteer.core.user.domain.UserWithEmail;

public interface GetUserWithEmailPort {
    UserWithEmail getUserWithEmail(long userId);
}
