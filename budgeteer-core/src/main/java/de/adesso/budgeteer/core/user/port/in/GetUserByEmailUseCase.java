package de.adesso.budgeteer.core.user.port.in;

import de.adesso.budgeteer.core.user.domain.User;

import java.util.Optional;

public interface GetUserByEmailUseCase {
    Optional<User> getUserByEmail(String mail);
}
