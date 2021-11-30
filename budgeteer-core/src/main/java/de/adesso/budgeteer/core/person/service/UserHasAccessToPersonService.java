package de.adesso.budgeteer.core.person.service;

import de.adesso.budgeteer.core.person.port.in.UserHasAccessToPersonUseCase;
import de.adesso.budgeteer.core.person.port.out.UserHasAccessToPersonPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserHasAccessToPersonService implements UserHasAccessToPersonUseCase {
    private final UserHasAccessToPersonPort userHasAccessToPersonPort;

    @Override
    public boolean userHasAccessToPerson(String username, long personId) {
        return userHasAccessToPersonPort.userHasAccessToPerson(username, personId);
    }
}
