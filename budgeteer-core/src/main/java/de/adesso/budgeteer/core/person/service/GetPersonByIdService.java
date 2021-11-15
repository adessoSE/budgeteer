package de.adesso.budgeteer.core.person.service;

import de.adesso.budgeteer.core.person.domain.Person;
import de.adesso.budgeteer.core.person.port.in.GetPersonByIdUseCase;
import de.adesso.budgeteer.core.person.port.out.GetPersonByIdPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetPersonByIdService implements GetPersonByIdUseCase {
    private final GetPersonByIdPort getPersonByIdPort;

    @Override
    public Optional<Person> getPersonById(Long personId) {
        return getPersonByIdPort.getPersonById(personId);
    }
}
