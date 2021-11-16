package de.adesso.budgeteer.core.budget.port.out;

import de.adesso.budgeteer.core.budget.domain.Budget;
import lombok.Value;
import org.joda.money.Money;

import java.util.List;

public interface CreateBudgetEntityPort {
    Budget createBudgetEntity(CreateBudgetCommandEntity command);

    @Value
    class CreateBudgetCommandEntity {
        long contractId;
        String name;
        String description;
        String importKey;
        Money total;
        Money limit;
        List<String> tags;
    }
}
