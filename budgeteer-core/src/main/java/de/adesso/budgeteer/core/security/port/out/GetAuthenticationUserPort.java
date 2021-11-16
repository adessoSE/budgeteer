package de.adesso.budgeteer.core.security.port.out;

import de.adesso.budgeteer.core.security.domain.AuthenticationUser;

import java.util.Optional;

public interface GetAuthenticationUserPort {
    Optional<AuthenticationUser> getAuthenticationUser(String username);
}
