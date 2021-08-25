package de.adesso.budgeteer.core.contract.service;

import de.adesso.budgeteer.core.contract.domain.Contract;
import de.adesso.budgeteer.core.contract.port.out.GetContractsInProjectPort;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetContractsInProjectServiceTest {

    @InjectMocks private GetContractsInProjectService getContractsInProjectService;
    @Mock private GetContractsInProjectPort getContractsInProjectPort;

    @Test
    void shouldReturnAllContractsInProject() {
        var projectId = 5L;
        var expectedContracts = List.of(
                new Contract(1L, 2L, "internalNumber1", "name1", Contract.Type.FIXED_PRICE, LocalDate.of(2021, 8, 25), Money.of(CurrencyUnit.EUR, 1000L), Money.of(CurrencyUnit.EUR, 500L), Money.of(CurrencyUnit.EUR, 500L), BigDecimal.valueOf(19L), Collections.emptyMap(), null),
                new Contract(3L, 4L, "internalNumber2", "name2", Contract.Type.FIXED_PRICE, LocalDate.of(2021, 5, 3), Money.of(CurrencyUnit.EUR, 10000L), Money.of(CurrencyUnit.EUR, 1500L), Money.of(CurrencyUnit.EUR, 8500L), BigDecimal.valueOf(19L), Collections.emptyMap(), null)
        );
        when(getContractsInProjectPort.getContractsInProject(projectId)).thenReturn(expectedContracts);

        var returnedContracts = getContractsInProjectService.getContractsInProject(projectId);

        assertThat(returnedContracts).isEqualTo(expectedContracts);
    }

    @Test
    void shouldReturnNoContractsIfProjectHasNoContracts() {
        var projectId = 1L;
        when(getContractsInProjectService.getContractsInProject(projectId)).thenReturn(Collections.emptyList());

        var returnedContracts = getContractsInProjectService.getContractsInProject(projectId);

        assertThat(returnedContracts).isEmpty();
    }
}
