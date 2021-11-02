package de.adesso.budgeteer.core.invoice.service;

import de.adesso.budgeteer.core.invoice.port.out.DeleteInvoiceByIdPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteInvoiceByIdServiceTest {
    @Mock
    private DeleteInvoiceByIdPort deleteInvoiceByIdPort;
    @InjectMocks
    private DeleteInvoiceByIdService deleteInvoiceByIdService;

    @Test
    void shouldDeleteInvoice() {
        var invoiceId = 1L;
        doNothing().when(deleteInvoiceByIdPort).deleteInvoiceById(invoiceId);

        deleteInvoiceByIdService.deleteInvoiceById(1L);

        verify(deleteInvoiceByIdPort).deleteInvoiceById(invoiceId);
    }
}