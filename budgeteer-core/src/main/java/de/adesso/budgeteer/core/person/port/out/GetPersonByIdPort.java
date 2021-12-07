package de.adesso.budgeteer.core.person.port.out;

import de.adesso.budgeteer.core.person.domain.Person;

import java.util.Optional;

public interface GetPersonByIdPort {
    Optional<Person> getPersonById(Long personId);
}
