package de.adesso.budgeteer.core.invoice.port.out;

public interface UserHasAccessToInvoicePort {
    boolean userHasAccessToInvoice(String username, long invoiceId);
}
