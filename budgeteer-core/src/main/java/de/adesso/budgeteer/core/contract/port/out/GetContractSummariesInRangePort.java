package de.adesso.budgeteer.core.contract.port.out;

import de.adesso.budgeteer.core.contract.domain.ContractSummary;

import java.time.LocalDate;
import java.util.List;

public interface GetContractSummariesInRangePort {
    List<ContractSummary> getContractSummariesInRange(long projectId, LocalDate from, LocalDate until);
}
