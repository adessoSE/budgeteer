package de.adesso.budgeteer.core.budget.port.in;

import de.adesso.budgeteer.core.budget.domain.Budget;
import lombok.Value;
import org.joda.money.Money;

import java.util.List;

public interface UpdateBudgetUseCase {
    Budget updateBudget(UpdateBudgetCommand updateBudgetCommand);

    @Value
    class UpdateBudgetCommand {
        long budgetId;
        long contractId;
        String name;
        String description;
        String importKey;
        Money total;
        Money limit;
        List<String> tags;
    }
}
