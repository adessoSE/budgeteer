package de.adesso.budgeteer.core.budget.service;

import de.adesso.budgeteer.core.budget.port.out.DeleteBudgetByIdPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteBudgetByIdServiceTest {

    @Mock private DeleteBudgetByIdPort deleteBudgetByIdPort;
    @InjectMocks private DeleteBudgetByIdService deleteBudgetByIdService;

    @Test
    void shouldDeleteBudget() {
        var budgetId = 1L;
        doNothing().when(deleteBudgetByIdPort).deleteBudgetById(budgetId);

        deleteBudgetByIdService.deleteBudgetById(budgetId);

        verify(deleteBudgetByIdPort).deleteBudgetById(budgetId);
    }
}
