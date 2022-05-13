package de.adesso.budgeteer.core.budget.service;

import de.adesso.budgeteer.core.budget.domain.Budget;
import de.adesso.budgeteer.core.budget.port.in.GetBudgetsInContractUseCase;
import de.adesso.budgeteer.core.budget.port.out.GetBudgetsInContractPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetBudgetsInContractService implements GetBudgetsInContractUseCase {
  private final GetBudgetsInContractPort getBudgetsInContractPort;

  @Override
  public List<Budget> getBudgetsInContract(long contractId) {
    return getBudgetsInContractPort.getBudgetsInContract(contractId);
  }
}
