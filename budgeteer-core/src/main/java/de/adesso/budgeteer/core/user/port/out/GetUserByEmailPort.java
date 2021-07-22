package de.adesso.budgeteer.core.user.port.out;

import de.adesso.budgeteer.core.user.domain.User;

import java.util.Optional;

public interface GetUserByEmailPort {
    Optional<User> getUserByEmail(String email);
}
