package de.adesso.budgeteer.core.person.service;

import de.adesso.budgeteer.core.person.port.in.DeletePersonByIdUseCase;
import de.adesso.budgeteer.core.person.port.out.DeletePersonByIdPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletePersonByIdService implements DeletePersonByIdUseCase {
    private final DeletePersonByIdPort deletePersonByIdPort;

    @Override
    public void deletePersonById(long personId) {
        deletePersonByIdPort.deletePersonById(personId);
    }
}
