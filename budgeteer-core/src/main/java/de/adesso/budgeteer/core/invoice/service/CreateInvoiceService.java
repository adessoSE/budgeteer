package de.adesso.budgeteer.core.invoice.service;

import de.adesso.budgeteer.core.invoice.port.in.CreateInvoiceUseCase;
import de.adesso.budgeteer.core.invoice.port.out.CreateInvoiceEntityPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateInvoiceService implements CreateInvoiceUseCase {

    private final CreateInvoiceEntityPort createInvoiceEntityPort;

    @Override
    public void createInvoice(CreateInvoiceCommand command) {
        createInvoiceEntityPort.createInvoiceEntity(new CreateInvoiceEntityPort.CreateInvoiceEntityCommand(command.getInvoiceId(),
                command.getContractId(), command.getInvoiceName(), command.getAmountOwed(), command.getTaxRate(),
                command.getInternalNumber(), command.getYearMonth(), command.getPaidDate(), command.getDueDate(),
                command.getAttributes(), command.getLink(), command.getFile()));
    }
}
