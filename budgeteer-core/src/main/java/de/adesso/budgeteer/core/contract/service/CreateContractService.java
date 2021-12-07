package de.adesso.budgeteer.core.contract.service;

import de.adesso.budgeteer.core.contract.domain.Contract;
import de.adesso.budgeteer.core.contract.port.in.CreateContractUseCase;
import de.adesso.budgeteer.core.contract.port.out.CreateContractEntityPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateContractService implements CreateContractUseCase {

    private final CreateContractEntityPort createContractEntityPort;

    @Override
    public Contract createContract(CreateContractCommand command) {
        var createContractEntityCommand = new CreateContractEntityPort.CreateContractEntityCommand(
                command.getProjectId(),
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
        return createContractEntityPort.createContractEntity(createContractEntityCommand);
    }
}
