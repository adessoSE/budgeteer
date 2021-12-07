package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.core.user.port.in.GetUserByEmailUseCase;
import de.adesso.budgeteer.core.user.port.out.GetUserByEmailPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetUserByEmailService implements GetUserByEmailUseCase {

    private final GetUserByEmailPort getUserByEmailPort;

    @Override
    public Optional<User> getUserByEmail(String mail) {
        return getUserByEmailPort.getUserByEmail(mail);
    }
}
