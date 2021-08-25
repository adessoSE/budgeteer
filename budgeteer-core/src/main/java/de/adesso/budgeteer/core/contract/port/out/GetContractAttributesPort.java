package de.adesso.budgeteer.core.contract.port.out;

import java.util.Map;

public interface GetContractAttributesPort {
    Map<String, String> getContractAttributes(long projectId);
}
