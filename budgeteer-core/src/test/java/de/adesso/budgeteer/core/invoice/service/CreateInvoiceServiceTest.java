package de.adesso.budgeteer.core.invoice.service;

import de.adesso.budgeteer.core.common.FileAttachment;
import de.adesso.budgeteer.core.invoice.domain.Invoice;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateInvoiceServiceTest {
    @Mock
    private CreateInvoiceEntityPort createInvoiceEntityPort;

    @InjectMocks
    private CreateInvoiceService createInvoiceService;

    @Test
    void shouldCreateInvoice() {
        var command = new CreateInvoiceEntityPort.CreateInvoiceEntityCommand(
                1,
                "invoiceName",
                Money.of(CurrencyUnit.EUR, 1),
                BigDecimal.ONE,
                "internalNumber",
                YearMonth.of(2021, Month.JANUARY),
                LocalDate.of(2021, 10, 29),
                LocalDate.of(2021, 10, 29),
                new HashMap<>(),
                "link",
                new FileAttachment("fileName", new byte[]{})
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

        when(createInvoiceEntityPort.createInvoiceEntity(command)).thenReturn(expected);

        var returnedInvoice = createInvoiceService.createInvoice(new CreateInvoiceUseCase.CreateInvoiceCommand(
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