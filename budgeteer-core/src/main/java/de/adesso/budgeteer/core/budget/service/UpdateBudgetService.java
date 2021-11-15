package de.adesso.budgeteer.core.budget.service;

import de.adesso.budgeteer.core.budget.domain.Budget;
import de.adesso.budgeteer.core.budget.port.in.UpdateBudgetUseCase;
import de.adesso.budgeteer.core.budget.port.out.GetBudgetByIdPort;
import de.adesso.budgeteer.core.budget.port.out.IsUniqueImportKeyInProjectPort;
import de.adesso.budgeteer.core.budget.port.out.IsUniqueNameInProjectPort;
import de.adesso.budgeteer.core.budget.port.out.UpdateBudgetEntityPort;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateBudgetService implements UpdateBudgetUseCase {

    private final UpdateBudgetEntityPort updateBudgetEntityPort;
    private final IsUniqueNameInProjectPort isUniqueNameInProjectPort;
    private final IsUniqueImportKeyInProjectPort isUniqueImportKeyInProjectPort;
    private final GetBudgetByIdPort getBudgetByIdPort;

    @Override
    public Budget updateBudget(UpdateBudgetCommand command) {
        var budget = getBudgetByIdPort.getBudgetById(command.getBudgetId()).orElseThrow(IllegalArgumentException::new);
        if (!command.getName().equals(budget.getName()) && !isUniqueNameInProjectPort.isUniqueNameInProject(command.getProjectId(), command.getName())) {
            throw new IllegalArgumentException();
        }
        if (!command.getName().equals(budget.getName()) && !isUniqueImportKeyInProjectPort.isUniqueImportKeyInProject(command.getProjectId(), command.getImportKey())) {
            throw new IllegalArgumentException();
        }
        return updateBudgetEntityPort.updateBudgetEntity(new UpdateBudgetEntityPort.UpdateBudgetEntityCommand(
                command.getBudgetId(),
                command.getContractId(),
                command.getName(),
                command.getDescription(),
                command.getImportKey(),
                command.getTotal(),
                command.getLimit(),
                command.getTags()
        ));
    }
}
