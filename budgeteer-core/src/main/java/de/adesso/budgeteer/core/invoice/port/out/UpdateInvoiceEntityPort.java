package de.adesso.budgeteer.core.invoice.port.out;

import de.adesso.budgeteer.core.common.FileAttachment;
import de.adesso.budgeteer.core.invoice.domain.Invoice;
import lombok.Value;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

public interface UpdateInvoiceEntityPort {
    Invoice updateInvoice(UpdateInvoiceEntityCommand command);

    @Value
    class UpdateInvoiceEntityCommand {
        long invoiceId;
        long contractId;
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
    }
}
