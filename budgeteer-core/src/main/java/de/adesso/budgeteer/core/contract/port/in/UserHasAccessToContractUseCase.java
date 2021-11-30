package de.adesso.budgeteer.core.contract.port.in;

public interface UserHasAccessToContractUseCase {
    boolean userHasAccessToContract(String username, long contractId);
}
