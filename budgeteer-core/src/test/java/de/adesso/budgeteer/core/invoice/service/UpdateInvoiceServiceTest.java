package de.adesso.budgeteer.core.invoice.service;

import de.adesso.budgeteer.core.common.FileAttachment;
import de.adesso.budgeteer.core.invoice.domain.Invoice;
import de.adesso.budgeteer.core.invoice.port.in.UpdateInvoiceUseCase;
import de.adesso.budgeteer.core.invoice.port.out.UpdateInvoiceEntityPort;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateInvoiceServiceTest {

    @Mock private UpdateInvoiceEntityPort updateInvoiceEntityPort;
    @InjectMocks private UpdateInvoiceService updateInvoiceService;

    @Test
    void shouldUpdateInvoice() {
        var command = new UpdateInvoiceEntityPort.UpdateInvoiceEntityCommand(
                1L,
                1L,
                "invoiceName",
                Money.of(CurrencyUnit.EUR, 500),
                BigDecimal.ONE,
                "internalNumber",
                YearMonth.of(2021, 11),
                LocalDate.of(2021, 11, 2),
                LocalDate.of(2021, 11, 2),
                new HashMap<>(),
                "link",
                new FileAttachment("filename", new byte[]{})
        );

        var expected = new Invoice(
                1L,
                command.getContractId(),
                "contract",
                command.getInvoiceName(),
                command.getAmountOwed(),
                command.getTaxRate(),
                command.getInternalNumber(),
                command.getYearMonth(),
                command.getPaidDate(),
                command.getDueDate(),
                command.getAttributes(),
                command.getLink(),
                command.getFile()
        );

        when(updateInvoiceEntityPort.updateInvoice(command)).thenReturn(expected);

        var returnedInvoice = updateInvoiceService.updateInvoice(new UpdateInvoiceUseCase.UpdateInvoiceCommand(
           command.getInvoiceId(),
           command.getContractId(),
           command.getInvoiceName(),
           command.getAmountOwed(),
           command.getTaxRate(),
           command.getInternalNumber(),
           command.getYearMonth(),
           command.getPaidDate(),
           command.getDueDate(),
           command.getAttributes(),
           command.getLink(),
           command.getFile()
        ));

        assertThat(returnedInvoice).isEqualTo(expected);
    }

}