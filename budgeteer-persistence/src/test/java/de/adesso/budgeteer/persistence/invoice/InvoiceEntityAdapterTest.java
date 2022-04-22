package de.adesso.budgeteer.persistence.invoice;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import de.adesso.budgeteer.core.invoice.domain.Invoice;
import de.adesso.budgeteer.core.invoice.port.out.CreateInvoiceEntityPort;
import de.adesso.budgeteer.persistence.contract.ContractAdapter;
import de.adesso.budgeteer.persistence.contract.ContractEntity;
import de.adesso.budgeteer.persistence.contract.ContractRepository;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Optional;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InvoiceEntityAdapterTest {

  @Mock private InvoiceRepository invoiceRepository;
  @Mock private InvoiceMapper invoiceMapper;
  @Mock private ContractRepository contractRepository;
  @Mock private ContractAdapter contractAdapter;
  @InjectMocks private InvoiceEntityAdapter invoiceEntityAdapter;

  @Test
  void shouldCreateInvoiceWithMinimalValidCreateInvoiceEntityCommand() {
    var command =
        new CreateInvoiceEntityPort.CreateInvoiceEntityCommand(
            1L,
            "invoiceName",
            Money.of(CurrencyUnit.EUR, 0),
            BigDecimal.ZERO,
            "internalNumber",
            YearMonth.of(2020, 12),
            null,
            null,
            null,
            null,
            null);
    var contractEntity = new ContractEntity();
    var invoiceEntity = new InvoiceEntity();
    invoiceEntity.setContract(contractEntity);
    invoiceEntity.setName(command.getInvoiceName());
    invoiceEntity.setInvoiceSum(command.getAmountOwed());
    invoiceEntity.setInternalNumber(command.getInternalNumber());
    invoiceEntity.setYear(command.getYearMonth().getYear());
    invoiceEntity.setMonth(command.getYearMonth().getMonthValue());
    invoiceEntity.setDate(
        Date.valueOf(
            LocalDate.of(
                command.getYearMonth().getYear(), command.getYearMonth().getMonthValue(), 1)));
    var expected =
        new Invoice(
            2L,
            command.getContractId(),
            "contractName",
            command.getInvoiceName(),
            command.getAmountOwed(),
            command.getTaxRate(),
            command.getInternalNumber(),
            command.getYearMonth(),
            command.getPaidDate(),
            command.getDueDate(),
            new HashMap<>(),
            command.getLink(),
            command.getFile());
    when(invoiceRepository.save(invoiceEntity)).thenReturn(invoiceEntity);
    when(invoiceMapper.mapToDomain(invoiceEntity)).thenReturn(expected);
    when(contractRepository.findById(command.getContractId()))
        .thenReturn(Optional.of(contractEntity));
    var result = invoiceEntityAdapter.createInvoiceEntity(command);
    assertThat(result).isEqualTo(expected);
  }
}
