package de.adesso.budgeteer.rest.contract.model;

import de.adesso.budgeteer.core.common.Attachment;
import de.adesso.budgeteer.core.contract.domain.Contract;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.*;
import lombok.Data;
import org.joda.money.Money;

@Data
public class CreateContractModel {
  @NotEmpty String internalNumber;

  @NotEmpty String name;

  @NotNull Contract.Type type;

  @NotNull LocalDate startDate;

  @NotNull Money budget;

  @NotNull
  @DecimalMin(value = "0.0")
  BigDecimal taxRate;

  Map<String, String> attributes = new HashMap<>();
  Attachment attachment;
}
