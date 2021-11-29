package de.adesso.budgeteer.rest.invoice.model;

import de.adesso.budgeteer.core.common.FileAttachment;
import lombok.Value;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

@Value
public class InvoiceModel {
    long invoiceId;
    String invoiceName;
    long contractId;
    String contractName;
    Money amountOwed;
    BigDecimal taxRate;
    String internalNumber;
    YearMonth yearMonth;
    LocalDate paidDate;
    LocalDate dueDate;
    Map<String, String> attributes;
    String link;
    FileAttachment file;
}
