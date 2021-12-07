package de.adesso.budgeteer.core.invoice.service;

import de.adesso.budgeteer.core.invoice.port.in.UserHasAccessToInvoiceUseCase;
import de.adesso.budgeteer.core.invoice.port.out.UserHasAccessToInvoicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserHasAccessToInvoiceService implements UserHasAccessToInvoiceUseCase {
    private final UserHasAccessToInvoicePort userHasAccessToInvoicePort;

    @Override
    public boolean userHasAccessToInvoice(String username, long invoiceId) {
        return userHasAccessToInvoicePort.userHasAccessToInvoice(username, invoiceId);
    }
}
