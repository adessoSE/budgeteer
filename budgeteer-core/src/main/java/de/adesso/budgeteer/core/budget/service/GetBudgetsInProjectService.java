package de.adesso.budgeteer.core.budget.service;

import de.adesso.budgeteer.core.budget.domain.Budget;
import de.adesso.budgeteer.core.budget.port.in.GetBudgetsInProjectUseCase;
import de.adesso.budgeteer.core.budget.port.out.GetBudgetsInProjectPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetBudgetsInProjectService implements GetBudgetsInProjectUseCase{
    private final GetBudgetsInProjectPort getBudgetsInProjectPort;

    @Override
    public List<Budget> getBudgetsInProject(long projectId) {
        return getBudgetsInProjectPort.getBudgetsInProject(projectId);
    }
}
