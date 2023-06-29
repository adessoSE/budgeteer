package de.adesso.budgeteer.rest.budget;

import de.adesso.budgeteer.core.budget.domain.Budget;
import de.adesso.budgeteer.rest.budget.model.BudgetModel;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class BudgetModelMapper {
  public BudgetModel toModel(Budget budget) {
    return new BudgetModel(
        budget.getId(),
        budget.getContractId(),
        budget.getName(),
        budget.getName(),
        budget.getDescription(),
        budget.getImportKey(),
        budget.getTotal(),
        budget.getSpent(),
        budget.getRemaining(),
        budget.getAverageDailyRate(),
        budget.getLimit(),
        budget.getLastUpdated(),
        budget.getTags());
  }

  public List<BudgetModel> toModel(List<Budget> budgets) {
    return budgets.stream().map(this::toModel).collect(Collectors.toList());
  }
}
