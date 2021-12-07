package de.adesso.budgeteer.core.budget.service;

import de.adesso.budgeteer.core.budget.domain.Budget;
import de.adesso.budgeteer.core.budget.port.in.GetBudgetByIdUseCase;
import de.adesso.budgeteer.core.budget.port.out.GetBudgetByIdPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetBudgetByIdService implements GetBudgetByIdUseCase {

    private final GetBudgetByIdPort getBudgetByIdPort;

    @Override
    public Optional<Budget> getBudgetById(long budgetId) {
        return getBudgetByIdPort.getBudgetById(budgetId);
    }
}
