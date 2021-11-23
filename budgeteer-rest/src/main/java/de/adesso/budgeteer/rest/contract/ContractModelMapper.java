package de.adesso.budgeteer.rest.contract;

import de.adesso.budgeteer.core.contract.domain.Contract;
import de.adesso.budgeteer.rest.contract.model.ContractModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContractModelMapper {
    public ContractModel toModel(Contract contract) {
        return new ContractModel(
                contract.getId(),
                contract.getProjectId(),
                contract.getInternalNumber(),
                contract.getName(),
                contract.getType(),
                contract.getStartDate(),
                contract.getBudget(),
                contract.getBudgetSpent(),
                contract.getBudgetLeft(),
                contract.getTaxRate(),
                contract.getAttributes(),
                contract.getAttachment()
        );
    }

    public List<ContractModel> toModel(List<Contract> contracts) {
        return contracts.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
