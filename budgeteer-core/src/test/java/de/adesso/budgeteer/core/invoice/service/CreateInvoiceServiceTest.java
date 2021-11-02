package de.adesso.budgeteer.core.invoice.service;

import de.adesso.budgeteer.core.common.FileAttachment;
import de.adesso.budgeteer.core.invoice.port.in.CreateInvoiceUseCase;
import de.adesso.budgeteer.core.invoice.port.out.CreateInvoiceEntityPort;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateInvoiceServiceTest {
    @Mock
    private CreateInvoiceEntityPort createInvoiceEntityPort;

    @InjectMocks
    private CreateInvoiceService createInvoiceService;

    @Test
    void shouldCreateInvoice() {
        var command = new CreateInvoiceUseCase.CreateInvoiceCommand(1, 1, "invoiceName",
                Money.of(CurrencyUnit.EUR, 1), BigDecimal.ONE, "internalNumber",
                YearMonth.of(2021, Month.JANUARY), LocalDate.of(2021, 10, 29),
                LocalDate.of(2021, 10, 29), new HashMap<>(), "link",
                new FileAttachment("fileName", new byte[]{}));

        var expected = new CreateInvoiceEntityPort.CreateInvoiceEntityCommand(command.getInvoiceId(), command.getContractId(),
                command.getInvoiceName(), command.getAmountOwed(), command.getTaxRate(), command.getInternalNumber(),
                command.getYearMonth(), command.getPaidDate(), command.getDueDate(), command.getAttributes(),
                command.getLink(), command.getFile());

        doNothing().when(createInvoiceEntityPort).createInvoiceEntity(expected);

        createInvoiceService.createInvoice(command);

        verify(createInvoiceEntityPort).createInvoiceEntity(expected);
    }
}