package de.adesso.budgeteer.core.person.service;

import de.adesso.budgeteer.core.person.port.in.UpdatePersonUseCase;
import de.adesso.budgeteer.core.person.port.out.UpdatePersonEntityPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdatePersonService implements UpdatePersonUseCase {
    private final UpdatePersonEntityPort updatePersonEntityPort;

    @Override
    public void updatePerson(UpdatePersonCommand command) {
        updatePersonEntityPort.updatePerson(new UpdatePersonEntityPort.UpdatePersonEntityCommand(command.getPersonId(), command.getPersonName(), command.getImportKey(), command.getDefaultDailyRate(), command.getRates()));
    }
}
