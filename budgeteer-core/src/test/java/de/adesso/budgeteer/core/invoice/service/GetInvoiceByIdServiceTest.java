package de.adesso.budgeteer.core.invoice.service;

import de.adesso.budgeteer.core.common.FileAttachment;
import de.adesso.budgeteer.core.invoice.domain.Invoice;
import de.adesso.budgeteer.core.invoice.port.out.GetInvoiceByIdPort;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetInvoiceByIdServiceTest {
    @Mock
    private GetInvoiceByIdPort getInvoiceByIdPort;
    @InjectMocks
    private GetInvoiceByIdService getInvoiceByIdService;

    @Test
    void shouldReturnInvoiceIfExists() {
        var expected = new Invoice(1L, 1L, "contractName", "invoiceName",
                Money.of(CurrencyUnit.EUR, 1), BigDecimal.ONE, "internalNumber",
                YearMonth.of(2021, 10), LocalDate.of(2021, 10, 29),
                LocalDate.of(2021, 10, 29), new HashMap<>(), "link",
                new FileAttachment("fileName", new byte[]{}));
        when(getInvoiceByIdPort.getInvoiceById(expected.getInvoiceId())).thenReturn(Optional.of(expected));

        var result = getInvoiceByIdService.getInvoiceById(expected.getInvoiceId());

        assertThat(result).contains(expected);
    }

    @Test
    void shouldReturnNullIfInvoiceDoesNotExist() {
        var invoiceId = 1L;
        when(getInvoiceByIdPort.getInvoiceById(invoiceId)).thenReturn(Optional.empty());

        var result = getInvoiceByIdService.getInvoiceById(invoiceId);

        assertThat(result).isEmpty();
    }
}