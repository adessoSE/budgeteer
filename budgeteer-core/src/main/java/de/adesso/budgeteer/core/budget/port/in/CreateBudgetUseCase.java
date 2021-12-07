package de.adesso.budgeteer.core.budget.port.in;

import de.adesso.budgeteer.core.budget.domain.Budget;
import lombok.Value;
import org.joda.money.Money;

import java.util.List;

public interface CreateBudgetUseCase {
    Budget createBudget(CreateBudgetCommand command);

    @Value
    class CreateBudgetCommand {
        long projectId;
        Long contractId;
        String name;
        String description;
        String importKey;
        Money total;
        Money limit;
        List<String> tags;
    }
}
