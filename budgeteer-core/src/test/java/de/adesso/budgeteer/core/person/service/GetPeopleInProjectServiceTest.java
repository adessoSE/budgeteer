package de.adesso.budgeteer.core.person.service;

import de.adesso.budgeteer.core.person.domain.Person;
import de.adesso.budgeteer.core.person.port.out.GetPeopleInProjectPort;
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
class GetPeopleInProjectServiceTest {
    @Mock private GetPeopleInProjectPort getPeopleInProjectPort;
    @InjectMocks private GetPeopleInProjectService getPeopleInProjectService;

    @Test
    void shouldReturnAllPeopleInProject() {
        var projectId = 5L;
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
        when(getPeopleInProjectPort.getPeopleInProject(projectId)).thenReturn(expectedPeople);

        var returnedPeople = getPeopleInProjectService.getPeopleInProject(projectId);

        assertThat(returnedPeople).isEqualTo(expectedPeople);
    }

    @Test
    void shouldReturnNoPeopleIfProjectHasNoPeople() {
        var projectId = 5L;
        when(getPeopleInProjectPort.getPeopleInProject(projectId)).thenReturn(Collections.emptyList());

        var returnedPeople = getPeopleInProjectService.getPeopleInProject(projectId);

        assertThat(returnedPeople).isEmpty();
    }
}