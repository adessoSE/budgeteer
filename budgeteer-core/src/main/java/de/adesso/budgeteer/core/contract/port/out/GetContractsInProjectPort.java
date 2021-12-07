package de.adesso.budgeteer.core.contract.port.out;

import de.adesso.budgeteer.core.contract.domain.Contract;

import java.util.List;

public interface GetContractsInProjectPort {
    List<Contract> getContractsInProject(long projectId);
}
