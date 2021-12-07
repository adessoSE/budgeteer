package de.adesso.budgeteer.core.contract.port.in;

import de.adesso.budgeteer.core.contract.domain.Contract;

import java.util.List;

public interface GetContractsInProjectUseCase {
    List<Contract> getContractsInProject(long projectId);
}
