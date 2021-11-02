package de.adesso.budgeteer.core.invoice.port.out;

import de.adesso.budgeteer.core.invoice.domain.Invoice;

import java.util.List;

public interface GetInvoicesInProjectPort {
    List<Invoice> getInvoicesInProject(long projectId);
}
