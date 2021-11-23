package de.adesso.budgeteer.core.contract.service;

import de.adesso.budgeteer.core.contract.domain.Contract;
import de.adesso.budgeteer.core.contract.port.in.UpdateContractUseCase;
import de.adesso.budgeteer.core.contract.port.out.UpdateContractEntityPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateContractService implements UpdateContractUseCase {

    private final UpdateContractEntityPort updateContractEntityPort;

    @Override
    public Contract updateContract(UpdateContractCommand command) {
        var updateContractEntityCommand = new UpdateContractEntityPort.UpdateContractEntityCommand(
                command.getContractId(),
                command.getName(),
                command.getInternalNumber(),
                command.getStartDate(),
                command.getType(),
                command.getBudget(),
                command.getTaxRate(),
                command.getAttributes(),
                command.getLink(),
                command.getFileName(),
                command.getFile()
        );
        return updateContractEntityPort.updateContractEntity(updateContractEntityCommand);
    }
}
