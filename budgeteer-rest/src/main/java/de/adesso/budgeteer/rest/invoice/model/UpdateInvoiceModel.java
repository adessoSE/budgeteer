package de.adesso.budgeteer.rest.invoice.model;

import de.adesso.budgeteer.core.common.FileAttachment;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;
import org.joda.money.Money;

@Data
public class UpdateInvoiceModel {
  @Positive long contractId;
  @NotEmpty String name;
  @NotNull Money amountOwed;

  @DecimalMin(value = "0.0")
  BigDecimal taxRate;

  @NotEmpty String internalNumber;
  @NotNull YearMonth yearMonth;
  LocalDate paidDate;
  LocalDate dueDate;
  Map<String, String> attributes;
  String link;
  FileAttachment file;
}
