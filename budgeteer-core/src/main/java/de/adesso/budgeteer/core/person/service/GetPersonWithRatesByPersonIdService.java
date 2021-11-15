package de.adesso.budgeteer.core.person.service;

import de.adesso.budgeteer.core.person.domain.PersonWithRates;
import de.adesso.budgeteer.core.person.port.in.GetPersonWithRatesByPersonIdUseCase;
import de.adesso.budgeteer.core.person.port.out.GetPersonWithRatesByPersonIdPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetPersonWithRatesByPersonIdService implements GetPersonWithRatesByPersonIdUseCase {
    private final GetPersonWithRatesByPersonIdPort getPersonWithRatesByPersonIdPort;

    @Override
    public PersonWithRates getPersonWithRatesByPersonId(Long personId) {
        return getPersonWithRatesByPersonIdPort.getPersonWithRatesByPersonId(personId);
    }
}
