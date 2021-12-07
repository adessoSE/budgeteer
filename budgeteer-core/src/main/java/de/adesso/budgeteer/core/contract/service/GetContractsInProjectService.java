package de.adesso.budgeteer.core.contract.service;

import de.adesso.budgeteer.core.contract.domain.Contract;
import de.adesso.budgeteer.core.contract.port.in.GetContractsInProjectUseCase;
import de.adesso.budgeteer.core.contract.port.out.GetContractsInProjectPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetContractsInProjectService implements GetContractsInProjectUseCase {

    private final GetContractsInProjectPort getContractsInProjectPort;

    @Override
    public List<Contract> getContractsInProject(long projectId) {
        return getContractsInProjectPort.getContractsInProject(projectId);
    }
}
