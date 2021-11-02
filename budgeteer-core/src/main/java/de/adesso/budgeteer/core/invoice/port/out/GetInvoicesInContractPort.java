package de.adesso.budgeteer.core.invoice.port.out;

import de.adesso.budgeteer.core.invoice.domain.Invoice;

import java.util.List;

public interface GetInvoicesInContractPort {
    List<Invoice> getInvoicesInContract(long contractId);
}
