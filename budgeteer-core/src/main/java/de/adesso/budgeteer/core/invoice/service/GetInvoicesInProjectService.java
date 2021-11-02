package de.adesso.budgeteer.core.invoice.service;

import de.adesso.budgeteer.core.invoice.domain.Invoice;
import de.adesso.budgeteer.core.invoice.port.in.GetInvoicesInProjectUseCase;
import de.adesso.budgeteer.core.invoice.port.out.GetInvoicesInProjectPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetInvoicesInProjectService implements GetInvoicesInProjectUseCase {
    private final GetInvoicesInProjectPort getInvoicesInProjectPort;

    @Override
    public List<Invoice> getInvoicesInProject(long projectId) {
        return getInvoicesInProjectPort.getInvoicesInProject(projectId);
    }
}
