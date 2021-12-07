package de.adesso.budgeteer.core.invoice.domain;

import de.adesso.budgeteer.core.common.FileAttachment;
import lombok.Value;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

@Value
public class Invoice {
    long invoiceId;
    long contractId;
    String contractName;
    String invoiceName;
    Money amountOwed;
    BigDecimal taxRate;
    String internalNumber;
    YearMonth yearMonth;
    LocalDate paidDate;
    LocalDate dueDate;
    Map<String, String> attributes;
    String link;
    FileAttachment file;

    public boolean isPaid() {
        return paidDate != null;
    }
}
