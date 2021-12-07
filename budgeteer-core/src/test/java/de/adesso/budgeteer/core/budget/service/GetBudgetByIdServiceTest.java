package de.adesso.budgeteer.core.budget.service;

import de.adesso.budgeteer.core.budget.domain.Budget;
import de.adesso.budgeteer.core.budget.port.out.GetBudgetByIdPort;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GetBudgetByIdServiceTest {

    @Mock private GetBudgetByIdPort getBudgetByIdPort;
    @InjectMocks private GetBudgetByIdService getBudgetByIdService;

    @Test
    void shouldReturnBudgetIfItExists() {
        var id = 1L;
        var expectedBudget = new Budget(id,
                2L,
                "name",
                "contractName",
                "description",
                Money.zero(CurrencyUnit.EUR),
                Money.zero(CurrencyUnit.EUR),
                Money.zero(CurrencyUnit.EUR),
                Money.zero(CurrencyUnit.EUR),
                Money.zero(CurrencyUnit.EUR),
                Money.zero(CurrencyUnit.EUR),
                new Date(),
                List.of());
        given(getBudgetByIdPort.getBudgetById(id)).willReturn(Optional.of(expectedBudget));

        var returned = getBudgetByIdService.getBudgetById(id);

        assertThat(returned).contains(expectedBudget);
    }
}