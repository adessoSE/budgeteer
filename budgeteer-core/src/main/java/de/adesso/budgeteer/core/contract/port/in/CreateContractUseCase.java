package de.adesso.budgeteer.core.contract.port.in;

import de.adesso.budgeteer.core.contract.domain.Contract;
import lombok.Value;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public interface CreateContractUseCase {
    Contract createContract(CreateContractCommand command);

    @Value
    class CreateContractCommand {
        long projectId;
        String name;
        String internalNumber;
        LocalDate startDate;
        Contract.Type type;
        Money budget;
        BigDecimal taxRate;
        Map<String, String> attributes;
        String link;
        String fileName;
        byte[] file;
    }
}
