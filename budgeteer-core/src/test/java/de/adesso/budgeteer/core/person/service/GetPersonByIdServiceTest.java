package de.adesso.budgeteer.core.person.service;

import de.adesso.budgeteer.core.person.domain.Person;
import de.adesso.budgeteer.core.person.port.out.GetPersonByIdPort;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPersonByIdServiceTest {
    @Mock private GetPersonByIdPort getPersonByIdPort;
    @InjectMocks private GetPersonByIdService getPersonByIdService;

    @Test
    void shouldReturnPersonIfExists() {
        var expected = new Person(
                1L,
                "name",
                Money.of(CurrencyUnit.EUR, 1.0),
                LocalDate.of(2021, 11, 8),
                LocalDate.of(2021, 11, 8),
                Money.of(CurrencyUnit.EUR, 1),
                1.0,
                Money.of(CurrencyUnit.EUR, 1)
        );
        when(getPersonByIdPort.getPersonById(expected.getId())).thenReturn(Optional.of(expected));

        var result = getPersonByIdService.getPersonById(expected.getId());

        assertThat(result).contains(expected);
    }

    @Test
    void shouldReturnNullIfPersonDoesNotExist() {
        var personId = 1L;
        when(getPersonByIdPort.getPersonById(personId)).thenReturn(Optional.empty());

        var result = getPersonByIdService.getPersonById(personId);

        assertThat(result).isEmpty();
    }
}