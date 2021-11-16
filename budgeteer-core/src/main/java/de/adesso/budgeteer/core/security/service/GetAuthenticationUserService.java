package de.adesso.budgeteer.core.security.service;

import de.adesso.budgeteer.core.security.domain.AuthenticationUser;
import de.adesso.budgeteer.core.security.port.in.GetAuthenticationUserUseCase;
import de.adesso.budgeteer.core.security.port.out.GetAuthenticationUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetAuthenticationUserService implements GetAuthenticationUserUseCase {

    private final GetAuthenticationUserPort getAuthenticationUserPort;

    @Override
    public Optional<AuthenticationUser> getAuthenticationUser(String username) {
        return getAuthenticationUserPort.getAuthenticationUser(username);
    }
}
