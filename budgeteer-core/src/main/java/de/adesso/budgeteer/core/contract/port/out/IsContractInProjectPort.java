package de.adesso.budgeteer.core.contract.port.out;

public interface IsContractInProjectPort {
    boolean isContractInProject(long projectId, long contractId);
}
