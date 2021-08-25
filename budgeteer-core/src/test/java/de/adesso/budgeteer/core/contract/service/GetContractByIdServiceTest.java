package de.adesso.budgeteer.core.contract.service;

import de.adesso.budgeteer.core.contract.domain.Contract;
import de.adesso.budgeteer.core.contract.port.out.GetContractByIdPort;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetContractByIdServiceTest {
    @InjectMocks private GetContractByIdService getContractByIdService;
    @Mock private GetContractByIdPort getContractByIdPort;

    @Test
    void shouldReturnContractWhenItExists() {
        var expectedContract = new Contract(1L, 2L, "internalNumber", "name", Contract.Type.FIXED_PRICE, LocalDate.of(2021, 8, 25), Money.of(CurrencyUnit.EUR, 1000L), Money.of(CurrencyUnit.EUR, 500L), Money.of(CurrencyUnit.EUR, 500L), BigDecimal.valueOf(19L), Collections.emptyMap(), null);
        when(getContractByIdPort.getContractById(expectedContract.getId())).thenReturn(Optional.of(expectedContract));

        var returnedContract = getContractByIdService.getContractById(expectedContract.getId());

        assertThat(returnedContract).contains(expectedContract);
    }

    @Test
    void shouldReturnNullWhenContractDoesNotExist() {
        var contractId = 1L;
        when(getContractByIdPort.getContractById(contractId)).thenReturn(Optional.empty());

        var returnedContract = getContractByIdService.getContractById(contractId);

        assertThat(returnedContract).isEmpty();
    }
}
