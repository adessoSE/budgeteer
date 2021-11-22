package de.adesso.budgeteer.core.budget.service;

import de.adesso.budgeteer.core.budget.domain.Budget;
import de.adesso.budgeteer.core.budget.port.in.CreateBudgetUseCase;
import de.adesso.budgeteer.core.budget.port.out.CreateBudgetEntityPort;
import de.adesso.budgeteer.core.budget.port.out.IsUniqueImportKeyInProjectPort;
import de.adesso.budgeteer.core.budget.port.out.IsUniqueNameInProjectPort;
import de.adesso.budgeteer.core.contract.port.out.IsContractInProjectPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateBudgetService implements CreateBudgetUseCase {

    private final CreateBudgetEntityPort createBudgetEntityPort;
    private final IsUniqueNameInProjectPort isUniqueNameInProjectPort;
    private final IsUniqueImportKeyInProjectPort isUniqueImportKeyInProjectPort;
    private final IsContractInProjectPort isContractInProjectPort;

    @Override
    public Budget createBudget(CreateBudgetCommand command) {
        if (command.getContractId() != null && !isContractInProjectPort.isContractInProject(command.getProjectId(), command.getContractId())) {
            throw new IllegalArgumentException(); // TODO: Proper error handling
        }
        if (!isUniqueNameInProjectPort.isUniqueNameInProject(command.getProjectId(), command.getName())) {
            throw new IllegalArgumentException(); // TODO: Proper error handling
        }
        if (!isUniqueImportKeyInProjectPort.isUniqueImportKeyInProject(command.getProjectId(), command.getImportKey())) {
            throw new IllegalArgumentException(); // TODO: Proper error handling
        }
        return createBudgetEntityPort.createBudgetEntity(new CreateBudgetEntityPort.CreateBudgetCommandEntity(
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
