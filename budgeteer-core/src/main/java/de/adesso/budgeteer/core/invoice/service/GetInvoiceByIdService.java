package de.adesso.budgeteer.core.invoice.service;

import de.adesso.budgeteer.core.invoice.domain.Invoice;
import de.adesso.budgeteer.core.invoice.port.in.GetInvoiceByIdUseCase;
import de.adesso.budgeteer.core.invoice.port.out.GetInvoiceByIdPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetInvoiceByIdService implements GetInvoiceByIdUseCase {
    private final GetInvoiceByIdPort getInvoiceByIdPort;

    @Override
    public Optional<Invoice> getInvoiceById(long invoiceId) {
        return getInvoiceByIdPort.getInvoiceById(invoiceId);
    }
}
