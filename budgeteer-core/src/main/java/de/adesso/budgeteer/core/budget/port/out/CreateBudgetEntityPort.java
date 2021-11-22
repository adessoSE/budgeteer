package de.adesso.budgeteer.core.budget.port.out;

import de.adesso.budgeteer.core.budget.domain.Budget;
import lombok.Value;
import org.joda.money.Money;

import java.util.List;
import java.util.Optional;

public interface CreateBudgetEntityPort {
    Budget createBudgetEntity(CreateBudgetCommandEntity command);

    @Value
    class CreateBudgetCommandEntity {
        long projectId;
        Long contractId;
        String name;
        String description;
        String importKey;
        Money total;
        Money limit;
        List<String> tags;

        public Optional<Long> getContractId() {
            return Optional.ofNullable(contractId);
        }
    }
}
