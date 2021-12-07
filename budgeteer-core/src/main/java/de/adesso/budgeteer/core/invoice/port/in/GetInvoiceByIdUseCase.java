package de.adesso.budgeteer.core.invoice.port.in;

import de.adesso.budgeteer.core.invoice.domain.Invoice;

import java.util.Optional;

public interface GetInvoiceByIdUseCase {
    Optional<Invoice> getInvoiceById(long invoiceId);
}
