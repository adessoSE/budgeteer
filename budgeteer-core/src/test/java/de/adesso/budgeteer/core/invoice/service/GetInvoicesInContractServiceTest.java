package de.adesso.budgeteer.core.invoice.service;

import de.adesso.budgeteer.core.common.FileAttachment;
import de.adesso.budgeteer.core.invoice.domain.Invoice;
import de.adesso.budgeteer.core.invoice.port.out.GetInvoicesInContractPort;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetInvoicesInContractServiceTest {
    @Mock private GetInvoicesInContractPort getInvoicesInContractPort;
    @InjectMocks private GetInvoicesInContractService getInvoicesInContractService;

    @Test
    void shouldReturnAllInvoicesInContract() {
        var contractId = 5L;
        var expectedInvoices = List.of(
                new Invoice(1L, 2L, "contractName", "invoiceName", Money.of(CurrencyUnit.EUR, 100), BigDecimal.ONE, "internalNumber", YearMonth.of(2021, 11), LocalDate.of(2021, 11, 2), LocalDate.of(2021, 11, 2), new HashMap<>(), "link", new FileAttachment("fileName", new byte[]{})),
                new Invoice(2L, 4L, "contractName", "invoiceName", Money.of(CurrencyUnit.EUR, 100), BigDecimal.ONE, "internalNumber", YearMonth.of(2021, 11), LocalDate.of(2021, 11, 2), LocalDate.of(2021, 11, 2), new HashMap<>(), "link", new FileAttachment("fileName", new byte[]{}))
        );
        when(getInvoicesInContractPort.getInvoicesInContract(contractId)).thenReturn(expectedInvoices);

        var returnedInvoices = getInvoicesInContractService.getInvoicesInContract(contractId);

        assertThat(returnedInvoices).isEqualTo(expectedInvoices);
    }

    @Test
    void shouldReturnNoInvoicesIfContractHasNoInvoices() {
        var contractId = 1L;
        when(getInvoicesInContractService.getInvoicesInContract(contractId)).thenReturn(Collections.emptyList());

        var returnedInvoices = getInvoicesInContractService.getInvoicesInContract(contractId);

        assertThat(returnedInvoices).isEmpty();
    }
}