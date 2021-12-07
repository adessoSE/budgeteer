package de.adesso.budgeteer.core.person.service;

import de.adesso.budgeteer.core.person.domain.Person;
import de.adesso.budgeteer.core.person.port.in.CreatePersonUseCase;
import de.adesso.budgeteer.core.person.port.out.CreatePersonEntityPort;
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
class CreatePersonServiceTest {
    @Mock
    private CreatePersonEntityPort createPersonEntityPort;

    @InjectMocks
    private CreatePersonService createPersonService;

    @Test
    void shouldCreatePerson() {
        var command = new CreatePersonEntityPort.CreatePersonEntityCommand(
                "name",
                "importKey",
                Money.of(CurrencyUnit.EUR, 1),
                2L,
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

        when(createPersonEntityPort.createPersonEntity(command)).thenReturn(expected);

        var returnedPerson = createPersonService.createPerson(new CreatePersonUseCase.CreatePersonCommand(
           command.getPersonName(),
           command.getImportKey(),
           command.getDefaultDailyRate(),
           command.getProjectId(),
           command.getRates()
        ));

        assertThat(returnedPerson).isEqualTo(expected);
    }
}