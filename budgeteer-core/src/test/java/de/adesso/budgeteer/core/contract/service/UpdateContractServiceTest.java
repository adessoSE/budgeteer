package de.adesso.budgeteer.core.contract.service;

import de.adesso.budgeteer.core.contract.domain.Contract;
import de.adesso.budgeteer.core.contract.port.in.UpdateContractUseCase;
import de.adesso.budgeteer.core.contract.port.out.UpdateContractEntityPort;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateContractServiceTest {

    @InjectMocks private UpdateContractService updateContractService;
    @Mock private UpdateContractEntityPort updateContractEntityPort;

    @Test
    void shouldUpdateContract() {
        var command = new UpdateContractUseCase.UpdateContractCommand(
                1L,
                "new-name",
                "new-internalNumber",
                LocalDate.of(2020, 3, 20),
                Contract.Type.FIXED_PRICE,
                Money.of(CurrencyUnit.EUR, 1500L),
                BigDecimal.valueOf(19L),
                Map.of("key", "value"),
                "",
                "",
                new byte[]{}
        );
        var expectedCommand = new UpdateContractEntityPort.UpdateContractEntityCommand(
                command.getContractId(),
                command.getName(),
                command.getInternalNumber(),
                command.getStartDate(),
                command.getType(),
                command.getBudget(),
                command.getTaxRate(),
                command.getAttributes(),
                command.getLink(),
                command.getFileName(),
                command.getFile()
        );
        doNothing().when(updateContractEntityPort).updateContractEntity(expectedCommand);

        updateContractService.updateContract(command);

        verify(updateContractEntityPort).updateContractEntity(expectedCommand);
    }
}
