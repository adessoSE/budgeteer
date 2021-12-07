package de.adesso.budgeteer.rest.contract.model;

import de.adesso.budgeteer.core.common.Attachment;
import de.adesso.budgeteer.core.contract.domain.Contract;
import lombok.Value;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Value
public class ContractModel {
    long id;
    long projectId;
    String internalNumber;
    String name;
    Contract.Type type;
    LocalDate startDate;
    Money budget;
    Money budgetSpent;
    Money budgetLeft;
    BigDecimal taxRate;
    Map<String, String> attributes;
    Attachment attachment;
}
