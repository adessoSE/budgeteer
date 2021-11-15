package de.adesso.budgeteer.core.budget.service;

import de.adesso.budgeteer.core.budget.port.in.DeleteBudgetByIdUseCase;
import de.adesso.budgeteer.core.budget.port.out.DeleteBudgetByIdPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteBudgetByIdService implements DeleteBudgetByIdUseCase {
    private final DeleteBudgetByIdPort deleteBudgetByIdPort;

    @Override
    public void deleteBudgetById(long budgetId) {
        deleteBudgetByIdPort.deleteBudgetById(budgetId);
    }
}
