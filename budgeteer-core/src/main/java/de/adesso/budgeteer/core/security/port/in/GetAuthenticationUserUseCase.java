package de.adesso.budgeteer.core.security.port.in;

import de.adesso.budgeteer.core.security.domain.AuthenticationUser;

import java.util.Optional;

public interface GetAuthenticationUserUseCase {
    Optional<AuthenticationUser> getAuthenticationUser(String username);
}
