package de.adesso.budgeteer.core.contract.service;

import de.adesso.budgeteer.core.common.Attachment;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateContractServiceTest {

    @InjectMocks
    private UpdateContractService updateContractService;
    @Mock
    private UpdateContractEntityPort updateContractEntityPort;

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
        var expected = new Contract(
                command.getContractId(),
                15L,
                command.getInternalNumber(),
                command.getName(),
                command.getType(),
                command.getStartDate(),
                command.getBudget(),
                Money.zero(CurrencyUnit.EUR),
                command.getBudget(),
                command.getTaxRate(),
                command.getAttributes(),
                new Attachment(
                        command.getFileName(),
                        command.getLink(),
                        command.getFile()
                )
        );
        when(updateContractEntityPort.updateContractEntity(expectedCommand)).thenReturn(expected);

        var returned = updateContractService.updateContract(command);

        assertThat(returned).isEqualTo(expected);
    }
}
