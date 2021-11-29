package de.adesso.budgeteer.rest.invoice.model;

import de.adesso.budgeteer.core.common.FileAttachment;
import lombok.Data;
import org.joda.money.Money;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

@Data
public class CreateInvoiceModel {
    @NotEmpty
    String name;
    @Positive
    long contractId;
    @NotNull
    Money amountOwed;
    @DecimalMin(value = "0.0")
    BigDecimal taxRate;
    @NotEmpty
    String internalNumber;
    @NotNull
    YearMonth yearMonth;
    LocalDate paidDate;
    LocalDate dueDate;
    Map<String, String> attributes;
    String link;
    FileAttachment file;
}
