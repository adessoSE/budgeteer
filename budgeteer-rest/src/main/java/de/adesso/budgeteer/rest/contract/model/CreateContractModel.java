package de.adesso.budgeteer.rest.contract.model;

import de.adesso.budgeteer.core.common.Attachment;
import de.adesso.budgeteer.core.contract.domain.Contract;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.joda.money.Money;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
public class CreateContractModel {
    @Positive
    long projectId;

    @NotEmpty
    String internalNumber;

    @NotEmpty
    String name;

    @NotNull
    Contract.Type type;

    @NotNull
    LocalDate startDate;

    @NotNull
    Money budget;

    @NotNull
    @DecimalMin(value = "0.0")
    BigDecimal taxRate;
    Map<String, String> attributes = new HashMap<>();
    Attachment attachment;
}
