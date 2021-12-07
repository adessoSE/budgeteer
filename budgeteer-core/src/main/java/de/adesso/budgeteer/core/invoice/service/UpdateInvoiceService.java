package de.adesso.budgeteer.core.invoice.service;

import de.adesso.budgeteer.core.invoice.domain.Invoice;
import de.adesso.budgeteer.core.invoice.port.in.UpdateInvoiceUseCase;
import de.adesso.budgeteer.core.invoice.port.out.UpdateInvoiceEntityPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateInvoiceService implements UpdateInvoiceUseCase {
    private final UpdateInvoiceEntityPort updateInvoicePort;

    @Override
    public Invoice updateInvoice(UpdateInvoiceCommand command) {
        return updateInvoicePort.updateInvoice(new UpdateInvoiceEntityPort.UpdateInvoiceEntityCommand(command.getInvoiceId(),
                command.getContractId(), command.getInvoiceName(), command.getAmountOwed(), command.getTaxRate(),
                command.getInternalNumber(), command.getYearMonth(), command.getPaidDate(), command.getDueDate(),
                command.getAttributes(), command.getLink(), command.getFile()));
    }
}
