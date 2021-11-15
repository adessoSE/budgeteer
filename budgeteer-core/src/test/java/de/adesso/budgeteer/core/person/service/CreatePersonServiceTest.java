package de.adesso.budgeteer.core.person.service;

import de.adesso.budgeteer.core.person.port.in.CreatePersonUseCase;
import de.adesso.budgeteer.core.person.port.out.CreatePersonEntityPort;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreatePersonServiceTest {
    @Mock
    private CreatePersonEntityPort createPersonEntityPort;

    @InjectMocks
    private CreatePersonService createPersonService;

    @Test
    void shouldCreatePerson() {
        var command = new CreatePersonUseCase.CreatePersonCommand(
                1L,
                "name",
                "importKey",
                Money.of(CurrencyUnit.EUR, 1),
                2L,
                Collections.emptyList()
        );

        var expected = new CreatePersonEntityPort.CreatePersonEntityCommand(
                command.getPersonId(),
                command.getPersonName(),
                command.getImportKey(),
                command.getDefaultDailyRate(),
                command.getProjectId(),
                command.getRates()
        );

        doNothing().when(createPersonEntityPort).createPersonEntity(expected);

        createPersonService.createPerson(command);

        verify(createPersonEntityPort).createPersonEntity(expected);
    }
}