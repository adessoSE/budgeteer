package de.adesso.budgeteer.rest.invoice;

import de.adesso.budgeteer.core.invoice.domain.Invoice;
import de.adesso.budgeteer.rest.invoice.model.InvoiceModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InvoiceModelMapper {
    public InvoiceModel toModel(Invoice invoice) {
        return new InvoiceModel(
                invoice.getInvoiceId(),
                invoice.getInvoiceName(),
                invoice.getContractId(),
                invoice.getContractName(),
                invoice.getAmountOwed(),
                invoice.getTaxRate(),
                invoice.getInternalNumber(),
                invoice.getYearMonth(),
                invoice.getPaidDate(),
                invoice.getDueDate(),
                invoice.getAttributes(),
                invoice.getLink(),
                invoice.getFile()
        );
    }

    public List<InvoiceModel> toModel(List<Invoice> invoices) {
        return invoices.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
