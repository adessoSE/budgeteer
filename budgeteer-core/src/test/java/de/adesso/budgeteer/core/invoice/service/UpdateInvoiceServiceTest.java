package de.adesso.budgeteer.core.invoice.service;

import de.adesso.budgeteer.core.common.FileAttachment;
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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateInvoiceServiceTest {

    @Mock private UpdateInvoiceEntityPort updateInvoiceEntityPort;
    @InjectMocks private UpdateInvoiceService updateInvoiceService;

    @Test
    void shouldUpdateInvoice() {
        var command = new UpdateInvoiceUseCase.UpdateInvoiceCommand(
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

        var expectedCommand = new UpdateInvoiceEntityPort.UpdateInvoiceEntityCommand(
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
        );

        doNothing().when(updateInvoiceEntityPort).updateInvoice(expectedCommand);

        updateInvoiceService.updateInvoice(command);

        verify(updateInvoiceEntityPort).updateInvoice(expectedCommand);
    }

}