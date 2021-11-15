package de.adesso.budgeteer.core.person.port.in;

import de.adesso.budgeteer.core.person.domain.PersonWithRates;

public interface GetPersonWithRatesByPersonIdUseCase {
    PersonWithRates getPersonWithRatesByPersonId(Long personId);
}
