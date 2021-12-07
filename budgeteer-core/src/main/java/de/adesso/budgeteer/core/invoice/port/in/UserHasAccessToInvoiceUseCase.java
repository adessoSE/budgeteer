package de.adesso.budgeteer.core.invoice.port.in;

public interface UserHasAccessToInvoiceUseCase {
    boolean userHasAccessToInvoice(String username, long invoiceId);
}
