package de.adesso.budgeteer.core.person.port.out;

import de.adesso.budgeteer.core.person.domain.PersonWithRates;

public interface GetPersonWithRatesByPersonIdPort {
    PersonWithRates getPersonWithRatesByPersonId(Long personId);
}
