package de.adesso.budgeteer.core.invoice.service;

import de.adesso.budgeteer.core.common.FileAttachment;
import de.adesso.budgeteer.core.invoice.domain.Invoice;
import de.adesso.budgeteer.core.invoice.port.out.GetInvoicesInProjectPort;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetInvoicesInProjectServiceTest {
    @Mock private GetInvoicesInProjectPort getInvoicesInProjectPort;
    @InjectMocks private GetInvoicesInProjectService getInvoicesInProjectService;

    @Test
    void shouldReturnAllInvoicesInProject() {
        var projectId = 5L;
        var expectedInvoices = List.of(
                new Invoice(1L, 2L, "contractName", "invoiceName", Money.of(CurrencyUnit.EUR, 100), BigDecimal.ONE, "internalNumber", YearMonth.of(2021, 11), LocalDate.of(2021, 11, 2), LocalDate.of(2021, 11, 2), new HashMap<>(), "link", new FileAttachment("fileName", new byte[]{})),
                new Invoice(2L, 4L, "contractName", "invoiceName", Money.of(CurrencyUnit.EUR, 100), BigDecimal.ONE, "internalNumber", YearMonth.of(2021, 11), LocalDate.of(2021, 11, 2), LocalDate.of(2021, 11, 2), new HashMap<>(), "link", new FileAttachment("fileName", new byte[]{}))
        );
        when(getInvoicesInProjectPort.getInvoicesInProject(projectId)).thenReturn(expectedInvoices);

        var returnedInvoices = getInvoicesInProjectService.getInvoicesInProject(projectId);

        assertThat(returnedInvoices).isEqualTo(expectedInvoices);
    }

    @Test
    void shouldReturnNoInvoicesIfProjectHasNoInvoices() {
        var projectId = 5L;
        when(getInvoicesInProjectService.getInvoicesInProject(projectId)).thenReturn(Collections.emptyList());

        var returnedInvoices = getInvoicesInProjectService.getInvoicesInProject(projectId);

        assertThat(returnedInvoices).isEmpty();
    }
}