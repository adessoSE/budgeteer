package de.adesso.budgeteer.core.invoice.port.in;

import de.adesso.budgeteer.core.invoice.domain.Invoice;

import java.util.List;

public interface GetInvoicesInProjectUseCase {
    List<Invoice> getInvoicesInProject(long projectId);
}
