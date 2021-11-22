package de.adesso.budgeteer.core.budget.port.out;

import de.adesso.budgeteer.core.budget.domain.Budget;
import lombok.Value;
import org.joda.money.Money;

import java.util.List;

public interface UpdateBudgetEntityPort {
    Budget updateBudgetEntity(UpdateBudgetEntityCommand updateBudgetEntityCommand);

    @Value
    class UpdateBudgetEntityCommand {
        long budgetId;
        Long contractId;
        String name;
        String description;
        String importKey;
        Money total;
        Money limit;
        List<String> tags;
    }
}
