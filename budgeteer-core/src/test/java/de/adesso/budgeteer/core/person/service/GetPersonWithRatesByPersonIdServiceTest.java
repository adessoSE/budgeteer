package de.adesso.budgeteer.core.person.service;

import de.adesso.budgeteer.core.person.domain.PersonWithRates;
import de.adesso.budgeteer.core.person.port.out.GetPersonWithRatesByPersonIdPort;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPersonWithRatesByPersonIdServiceTest {
    @Mock private GetPersonWithRatesByPersonIdPort getPersonWithRatesByPersonIdPort;
    @InjectMocks private GetPersonWithRatesByPersonIdService getPersonWithRatesByPersonIdService;

    @Test
    void shouldReturnPersonWithRatesIfExists() {
        var expected = new PersonWithRates(
                1L,
                "name", "importKey",
                Money.of(CurrencyUnit.EUR, 1),
                Collections.emptyList()
        );
        when(getPersonWithRatesByPersonIdPort.getPersonWithRatesByPersonId(expected.getPersonId())).thenReturn(expected);

        var result = getPersonWithRatesByPersonIdService.getPersonWithRatesByPersonId(expected.getPersonId());

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldReturnNullIfPersonWithRatesDoesNotExist() {
        var personId = 1L;
        when(getPersonWithRatesByPersonIdPort.getPersonWithRatesByPersonId(personId)).thenReturn(null);

        var result = getPersonWithRatesByPersonIdService.getPersonWithRatesByPersonId(personId);

        assertThat(result).isNull();
    }
}