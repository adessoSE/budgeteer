package de.adesso.budgeteer.core.budget.service;

import de.adesso.budgeteer.core.budget.port.in.CreateBudgetUseCase;
import de.adesso.budgeteer.core.budget.port.out.CreateBudgetEntityPort;
import de.adesso.budgeteer.core.budget.port.out.IsUniqueImportKeyInProjectPort;
import de.adesso.budgeteer.core.budget.port.out.IsUniqueNameInProjectPort;
import de.adesso.budgeteer.core.contract.port.out.IsContractInProjectPort;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateBudgetServiceTest {

    @Mock private CreateBudgetEntityPort createBudgetEntityPort;
    @Mock private IsUniqueNameInProjectPort isUniqueNameInProjectPort;
    @Mock private IsUniqueImportKeyInProjectPort isUniqueImportKeyInProjectPort;
    @Mock private IsContractInProjectPort isContractInProjectPort;
    @InjectMocks private CreateBudgetService createBudgetService;

    @Test
    void shouldCreateValidBudget() {
        var command = new CreateBudgetUseCase.CreateBudgetCommand(
                1L,
                2L,
                "name",
                "description",
                "importKey",
                Money.of(CurrencyUnit.EUR, 0),
                Money.of(CurrencyUnit.EUR, 1),
                new ArrayList<>()
        );

        var expected = new CreateBudgetEntityPort.CreateBudgetCommandEntity(
                command.getProjectId(),
                command.getContractId(),
                command.getName(),
                command.getDescription(),
                command.getImportKey(),
                command.getTotal(),
                command.getLimit(),
                command.getTags()
        );
        given(isUniqueImportKeyInProjectPort.isUniqueImportKeyInProject(command.getProjectId(), command.getImportKey())).willReturn(true);
        given(isUniqueNameInProjectPort.isUniqueNameInProject(command.getProjectId(), command.getName())).willReturn(true);
        given(isContractInProjectPort.isContractInProject(command.getProjectId(), command.getContractId())).willReturn(true);

        createBudgetService.createBudget(command);

        verify(createBudgetEntityPort).createBudgetEntity(expected);
    }

    @Test
    void shouldThrowExceptionWhenNameIsNotUniqueInProject() {
        var command = new CreateBudgetUseCase.CreateBudgetCommand(
                1L,
                2L,
                "name",
                "description",
                "importKey",
                Money.of(CurrencyUnit.EUR, 0),
                Money.of(CurrencyUnit.EUR, 1),
                new ArrayList<>()
        );

        given(isContractInProjectPort.isContractInProject(command.getProjectId(), command.getContractId())).willReturn(true);
        given(isUniqueNameInProjectPort.isUniqueNameInProject(command.getProjectId(), command.getName())).willReturn(false);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> createBudgetService.createBudget(command));
    }

    @Test
    void shouldThrowExceptionWhenContractIsNotInProject() {
        var command = new CreateBudgetUseCase.CreateBudgetCommand(
                1L,
                2L,
                "name",
                "description",
                "importKey",
                Money.of(CurrencyUnit.EUR, 0),
                Money.of(CurrencyUnit.EUR, 1),
                new ArrayList<>()
        );

        given(isContractInProjectPort.isContractInProject(command.getProjectId(), command.getContractId())).willReturn(false);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> createBudgetService.createBudget(command));
    }

    @Test
    void shouldThrowExceptionWhenImportKeyIsNotUniqueInProject() {
        var command = new CreateBudgetUseCase.CreateBudgetCommand(
                1L,
                2L,
                "name",
                "description",
                "importKey",
                Money.of(CurrencyUnit.EUR, 0),
                Money.of(CurrencyUnit.EUR, 1),
                new ArrayList<>()
        );

        given(isContractInProjectPort.isContractInProject(command.getProjectId(), command.getContractId())).willReturn(true);
        given(isUniqueNameInProjectPort.isUniqueNameInProject(command.getProjectId(), command.getName())).willReturn(true);
        given(isUniqueImportKeyInProjectPort.isUniqueImportKeyInProject(command.getProjectId(), command.getImportKey())).willReturn(false);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> createBudgetService.createBudget(command));
    }
}
