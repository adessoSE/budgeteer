package de.adesso.budgeteer.core.person.service;

import de.adesso.budgeteer.core.invoice.port.out.UpdateInvoiceEntityPort;
import de.adesso.budgeteer.core.person.port.in.UpdatePersonUseCase;
import de.adesso.budgeteer.core.person.port.out.UpdatePersonEntityPort;
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
class UpdatePersonServiceTest {
    @Mock private UpdatePersonEntityPort updatePersonEntityPort;
    @InjectMocks private UpdatePersonService updatePersonService;

    @Test
    void shouldUpdateService() {
        var command = new UpdatePersonUseCase.UpdatePersonCommand(
                1L,
                "name",
                "importKey",
                Money.of(CurrencyUnit.EUR, 1),
                Collections.emptyList()
        );

        var expectedCommand = new UpdatePersonEntityPort.UpdatePersonEntityCommand(
                command.getPersonId(),
                command.getPersonName(),
                command.getImportKey(),
                command.getDefaultDailyRate(),
                command.getRates()
        );

        doNothing().when(updatePersonEntityPort).updatePerson(expectedCommand);

        updatePersonService.updatePerson(command);

        verify(updatePersonEntityPort).updatePerson(expectedCommand);
    }
}