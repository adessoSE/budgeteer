package de.adesso.budgeteer.core.person.port.in;

import de.adesso.budgeteer.core.person.domain.Person;

import java.util.Optional;

public interface GetPersonByIdUseCase {
    Optional<Person> getPersonById(Long personId);
}
