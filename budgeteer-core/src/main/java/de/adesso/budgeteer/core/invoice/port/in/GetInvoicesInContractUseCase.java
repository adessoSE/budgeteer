package de.adesso.budgeteer.core.invoice.port.in;

import de.adesso.budgeteer.core.invoice.domain.Invoice;

import java.util.List;

public interface GetInvoicesInContractUseCase {
    List<Invoice> getInvoicesInContract(long contractId);
}
