package de.adesso.budgeteer.core.contract.port.out;

import de.adesso.budgeteer.core.contract.domain.Contract;

import java.util.Optional;

public interface GetContractByIdPort {
    Optional<Contract> getContractById(long contractId);
}
