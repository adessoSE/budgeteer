package de.adesso.budgeteer.core.invoice.service;

import de.adesso.budgeteer.core.invoice.port.in.DeleteInvoiceByIdUseCase;
import de.adesso.budgeteer.core.invoice.port.out.DeleteInvoiceByIdPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteInvoiceByIdService implements DeleteInvoiceByIdUseCase {
    private final DeleteInvoiceByIdPort deleteInvoiceByIdPort;

    @Override
    public void deleteInvoiceById(long invoiceId) {
        deleteInvoiceByIdPort.deleteInvoiceById(invoiceId);
    }
}
