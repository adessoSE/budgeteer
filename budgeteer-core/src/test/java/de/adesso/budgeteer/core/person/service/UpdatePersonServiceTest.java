package de.adesso.budgeteer.core.person.service;

import de.adesso.budgeteer.core.person.domain.Person;
import de.adesso.budgeteer.core.person.port.in.UpdatePersonUseCase;
import de.adesso.budgeteer.core.person.port.out.UpdatePersonEntityPort;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdatePersonServiceTest {
    @Mock private UpdatePersonEntityPort updatePersonEntityPort;
    @InjectMocks private UpdatePersonService updatePersonService;

    @Test
    void shouldUpdateService() {
        var command = new UpdatePersonEntityPort.UpdatePersonEntityCommand(
                1L,
                "name",
                "importKey",
                Money.of(CurrencyUnit.EUR, 1),
                Collections.emptyList()
        );

        var expected = new Person(
                1L,
                command.getPersonName(),
                Money.zero(CurrencyUnit.EUR),
                LocalDate.of(2021, 11, 29),
                LocalDate.of(2021, 11, 29),
                command.getDefaultDailyRate(),
                0.0,
                Money.zero(CurrencyUnit.EUR)
        );

        when(updatePersonEntityPort.updatePerson(command)).thenReturn(expected);

        var returnedPerson = updatePersonService.updatePerson(new UpdatePersonUseCase.UpdatePersonCommand(
           command.getPersonId(),
           command.getPersonName(),
           command.getImportKey(),
           command.getDefaultDailyRate(),
           command.getRates()
        ));

        assertThat(returnedPerson).isEqualTo(expected);
    }
}