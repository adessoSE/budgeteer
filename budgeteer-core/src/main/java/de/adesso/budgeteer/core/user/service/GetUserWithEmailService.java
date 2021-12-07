package de.adesso.budgeteer.core.user.service;

import de.adesso.budgeteer.core.user.domain.UserWithEmail;
import de.adesso.budgeteer.core.user.port.in.GetUserWithEmailUseCase;
import de.adesso.budgeteer.core.user.port.out.GetUserWithEmailPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserWithEmailService implements GetUserWithEmailUseCase {

    private final GetUserWithEmailPort getUserWithEmailPort;

    @Override
    public UserWithEmail getUserWithEmail(long userId) {
        return getUserWithEmailPort.getUserWithEmail(userId);
    }
}
