package de.adesso.budgeteer.core.person.service;

import de.adesso.budgeteer.core.person.domain.Person;
import de.adesso.budgeteer.core.person.port.out.GetPeopleInBudgetPort;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPeopleInBudgetServiceTest {
    @Mock private GetPeopleInBudgetPort getPeopleInBudgetPort;
    @InjectMocks private GetPeopleInBudgetService getPeopleInBudgetService;

    @Test
    void shouldReturnAllPeopleInBudget() {
        var budgetId = 5L;
        var expectedPeople = List.of(
                new Person(
                        1L,
                        "name",
                        Money.of(CurrencyUnit.EUR, 1),
                        LocalDate.of(2021, 11, 8),
                        LocalDate.of(2021, 11, 8),
                        Money.of(CurrencyUnit.EUR, 1),
                        1.0,
                        Money.of(CurrencyUnit.EUR, 1)
                ),
                new Person(
                        2L,
                        "name",
                        Money.of(CurrencyUnit.EUR, 1),
                        LocalDate.of(2021, 11, 8),
                        LocalDate.of(2021, 11, 8),
                        Money.of(CurrencyUnit.EUR, 1),
                        1.0,
                        Money.of(CurrencyUnit.EUR, 1)
                )
        );
        when(getPeopleInBudgetPort.getPeopleInBudget(budgetId)).thenReturn(expectedPeople);

        var returnedPeople = getPeopleInBudgetService.getPeopleInBudget(budgetId);

        assertThat(returnedPeople).isEqualTo(expectedPeople);
    }

    @Test
    void shouldReturnNoPeopleIfBudgetHasNoPeople() {
        var budgetId = 5L;
        when(getPeopleInBudgetPort.getPeopleInBudget(budgetId)).thenReturn(Collections.emptyList());

        var returnedPeople = getPeopleInBudgetService.getPeopleInBudget(budgetId);

        assertThat(returnedPeople).isEmpty();
    }
}