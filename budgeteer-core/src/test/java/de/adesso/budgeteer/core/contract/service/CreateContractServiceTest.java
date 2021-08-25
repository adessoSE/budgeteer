package de.adesso.budgeteer.core.contract.service;

import de.adesso.budgeteer.core.contract.domain.Contract;
import de.adesso.budgeteer.core.contract.port.in.CreateContractUseCase;
import de.adesso.budgeteer.core.contract.port.out.CreateContractEntityPort;
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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateContractServiceTest {

    @InjectMocks
    private CreateContractService createContractService;
    @Mock
    private CreateContractEntityPort createContractEntityPort;

    @Test
    void shouldCreateContract() {
        var command = new CreateContractUseCase.CreateContractCommand(
                1L,
                "project",
                "123",
                LocalDate.of(2021, 8, 25),
                Contract.Type.TIME_AND_MATERIAL,
                Money.of(CurrencyUnit.EUR, 1000L),
                BigDecimal.valueOf(19L),
                Collections.emptyMap(),
                "",
                "",
                new byte[]{}
        );
        var expected = new CreateContractEntityPort.CreateContractEntityCommand(
                command.getProjectId(),
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
        doNothing().when(createContractEntityPort).createContractEntity(expected);

        createContractService.createContract(command);

        verify(createContractEntityPort).createContractEntity(expected);
    }
}
