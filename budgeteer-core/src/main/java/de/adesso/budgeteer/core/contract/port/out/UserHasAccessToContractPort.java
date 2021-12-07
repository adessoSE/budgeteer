package de.adesso.budgeteer.core.contract.port.out;

public interface UserHasAccessToContractPort {
    boolean userHasAccessToContract(String username, long contractId);
}
