package de.adesso.budgeteer.core.invoice.port.out;

import de.adesso.budgeteer.core.invoice.domain.Invoice;

import java.util.Optional;

public interface GetInvoiceByIdPort {
    Optional<Invoice> getInvoiceById(long invoiceId);
}
