package de.adesso.budgeteer.core.contract.port.in;

import de.adesso.budgeteer.core.contract.domain.Contract;

import java.util.Optional;

public interface GetContractByIdUseCase {
    Optional<Contract> getContractById(long id);
}
