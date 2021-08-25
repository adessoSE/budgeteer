package de.adesso.budgeteer.core.contract.service;

import de.adesso.budgeteer.core.contract.port.out.DeleteContractPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteContractServiceTest {
    @InjectMocks private DeleteContractService deleteContractService;
    @Mock private DeleteContractPort deleteContractPort;

    @Test
    void shouldDeleteContract() {
        var contractId = 1L;
        doNothing().when(deleteContractPort).deleteContract(contractId);

        deleteContractService.deleteContract(contractId);

        verify(deleteContractPort).deleteContract(contractId);
    }
}
