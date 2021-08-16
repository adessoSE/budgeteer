package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.port.in.ProjectHasContractsUseCase;
import de.adesso.budgeteer.core.project.port.out.ProjectHasContractsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectHasContractsService implements ProjectHasContractsUseCase {

    private final ProjectHasContractsPort projectHasContractsPort;

    @Override
    public boolean projectHasContracts(long projectId) {
        return projectHasContractsPort.projectHasContracts(projectId);
    }
}
