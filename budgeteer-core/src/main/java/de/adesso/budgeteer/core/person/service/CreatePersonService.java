package de.adesso.budgeteer.core.person.service;

import de.adesso.budgeteer.core.person.port.in.CreatePersonUseCase;
import de.adesso.budgeteer.core.person.port.out.CreatePersonEntityPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePersonService implements CreatePersonUseCase {
    private final CreatePersonEntityPort createPersonEntityPort;

    @Override
    public void createPerson(CreatePersonCommand command) {
        createPersonEntityPort.createPersonEntity(new CreatePersonEntityPort.CreatePersonEntityCommand(command.getPersonId(), command.getPersonName(), command.getImportKey(), command.getDefaultDailyRate(), command.getProjectId(), command.getRates()));
    }
}
