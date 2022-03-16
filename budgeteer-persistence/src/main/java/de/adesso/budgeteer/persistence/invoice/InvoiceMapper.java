package de.adesso.budgeteer.persistence.invoice;

import de.adesso.budgeteer.core.common.FileAttachment;
import de.adesso.budgeteer.core.invoice.domain.Invoice;
import java.sql.Date;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {
  public Invoice mapToDomain(InvoiceEntity invoiceEntity) {
    var attributes =
        invoiceEntity.getDynamicFields().stream()
            .collect(
                Collectors.toMap(
                    invoiceFieldEntity -> invoiceFieldEntity.getField().getFieldName(),
                    InvoiceFieldEntity::getValue));

    return new Invoice(
        invoiceEntity.getId(),
        invoiceEntity.getContract().getId(),
        invoiceEntity.getContract().getName(),
        invoiceEntity.getName(),
        invoiceEntity.getInvoiceSum(),
        invoiceEntity.getContract().getTaxRate(),
        invoiceEntity.getInternalNumber(),
        YearMonth.of(invoiceEntity.getYear(), invoiceEntity.getMonth()),
        invoiceEntity.getPaidDate() == null
            ? null
            : Date.valueOf(invoiceEntity.getPaidDate().toString()).toLocalDate(),
        invoiceEntity.getDueDate() == null
            ? null
            : Date.valueOf(invoiceEntity.getDueDate().toString()).toLocalDate(),
        attributes,
        invoiceEntity.getLink(),
        invoiceEntity.getFileName() != null && invoiceEntity.getFile() != null
            ? new FileAttachment(invoiceEntity.getFileName(), invoiceEntity.getFile())
            : null);
  }

  public List<Invoice> mapToDomain(List<InvoiceEntity> invoiceEntities) {
    return invoiceEntities.stream().map(this::mapToDomain).collect(Collectors.toList());
  }
}
