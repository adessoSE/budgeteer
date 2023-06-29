package de.adesso.budgeteer.core.budget.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import de.adesso.budgeteer.core.budget.domain.Budget;
import de.adesso.budgeteer.core.budget.port.in.UpdateBudgetUseCase;
import de.adesso.budgeteer.core.budget.port.out.GetBudgetByIdPort;
import de.adesso.budgeteer.core.budget.port.out.IsUniqueImportKeyInProjectPort;
import de.adesso.budgeteer.core.budget.port.out.IsUniqueNameInProjectPort;
import de.adesso.budgeteer.core.budget.port.out.UpdateBudgetEntityPort;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateBudgetServiceTest {

  @Mock private UpdateBudgetEntityPort updateBudgetEntityPort;
  @Mock private IsUniqueNameInProjectPort isUniqueNameInProjectPort;
  @Mock private IsUniqueImportKeyInProjectPort isUniqueImportKeyInProjectPort;
  @Mock private GetBudgetByIdPort getBudgetByIdPort;
  @InjectMocks private UpdateBudgetService updateBudgetService;

  @Test
  void shouldUpdateValidBudget() {
    var budgetId = 1L;
    var budget =
        new Budget(
            budgetId,
            5L,
            "name",
            "contractName",
            "description",
            "importKey",
            null,
            null,
            null,
            null,
            null,
            new Date(),
            new ArrayList<>());
    var command =
        new UpdateBudgetUseCase.UpdateBudgetCommand(
            budgetId,
            3L,
            "new-name",
            "new-description",
            "new-importKey",
            Money.of(CurrencyUnit.EUR, 1),
            Money.of(CurrencyUnit.EUR, 2),
            new ArrayList<>());
    var expected =
        new UpdateBudgetEntityPort.UpdateBudgetEntityCommand(
            command.getBudgetId(),
            command.getContractId(),
            command.getName(),
            command.getDescription(),
            command.getImportKey(),
            command.getTotal(),
            command.getLimit(),
            command.getTags());
    given(getBudgetByIdPort.getBudgetById(budgetId)).willReturn(Optional.of(budget));
    given(
            (isUniqueImportKeyInProjectPort.isUniqueImportKeyInProjectByBudgetId(
                command.getBudgetId(), command.getImportKey())))
        .willReturn(true);
    given(
            (isUniqueNameInProjectPort.isUniqueNameInProjectByBudgetId(
                command.getBudgetId(), command.getName())))
        .willReturn(true);

    updateBudgetService.updateBudget(command);

    verify(updateBudgetEntityPort).updateBudgetEntity(expected);
  }

  @Test
  void shouldReturnUpdatedBudget() {
    var budgetId = 1L;
    var budget =
        new Budget(
            budgetId,
            5L,
            "name",
            "contractName",
            "description",
            "importKey",
            null,
            null,
            null,
            null,
            null,
            new Date(),
            new ArrayList<>());
    var command =
        new UpdateBudgetUseCase.UpdateBudgetCommand(
            budgetId,
            3L,
            "new-name",
            "new-description",
            "new-importKey",
            Money.of(CurrencyUnit.EUR, 1),
            Money.of(CurrencyUnit.EUR, 2),
            new ArrayList<>());
    var updateEntityCommand =
        new UpdateBudgetEntityPort.UpdateBudgetEntityCommand(
            command.getBudgetId(),
            command.getContractId(),
            command.getName(),
            command.getDescription(),
            command.getImportKey(),
            command.getTotal(),
            command.getLimit(),
            command.getTags());
    var expected =
        new Budget(
            budgetId,
            command.getContractId(),
            command.getName(),
            "new-contract",
            command.getDescription(),
            command.getImportKey(),
            command.getTotal(),
            Money.of(CurrencyUnit.EUR, 0),
            command.getTotal(),
            null,
            command.getLimit(),
            new Date(),
            new ArrayList<>());
    given(getBudgetByIdPort.getBudgetById(budgetId)).willReturn(Optional.of(budget));
    given(
            isUniqueImportKeyInProjectPort.isUniqueImportKeyInProjectByBudgetId(
                command.getBudgetId(), command.getImportKey()))
        .willReturn(true);
    given(
            isUniqueNameInProjectPort.isUniqueNameInProjectByBudgetId(
                command.getBudgetId(), command.getName()))
        .willReturn(true);
    given(updateBudgetEntityPort.updateBudgetEntity(updateEntityCommand)).willReturn(expected);

    var result = updateBudgetService.updateBudget(command);

    assertThat(result).isEqualTo(expected);
  }

  @Test
  void shouldThrowExceptionWhenNewNameIsDifferentButNotUniqueInProject() {
    var budgetId = 1L;
    var budget =
        new Budget(
            budgetId,
            5L,
            "name",
            "contractName",
            "description",
            "importKey",
            null,
            null,
            null,
            null,
            null,
            new Date(),
            new ArrayList<>());
    var command =
        new UpdateBudgetUseCase.UpdateBudgetCommand(
            budgetId,
            3L,
            "new-name",
            "new-description",
            "new-importKey",
            Money.of(CurrencyUnit.EUR, 1),
            Money.of(CurrencyUnit.EUR, 2),
            new ArrayList<>());
    given(getBudgetByIdPort.getBudgetById(budgetId)).willReturn(Optional.of(budget));
    given(
            isUniqueNameInProjectPort.isUniqueNameInProjectByBudgetId(
                command.getBudgetId(), command.getName()))
        .willReturn(false);

    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> updateBudgetService.updateBudget(command));
  }

  @Test
  void shouldThrowExceptionWhenNewImportKeyIsDifferentButNotUniqueInProject() {
    var budgetId = 1L;
    var budget =
        new Budget(
            budgetId,
            5L,
            "name",
            "contractName",
            "description",
            "importKey",
            null,
            null,
            null,
            null,
            null,
            new Date(),
            new ArrayList<>());
    var command =
        new UpdateBudgetUseCase.UpdateBudgetCommand(
            budgetId,
            3L,
            "new-name",
            "new-description",
            "new-importKey",
            Money.of(CurrencyUnit.EUR, 1),
            Money.of(CurrencyUnit.EUR, 2),
            new ArrayList<>());
    given(getBudgetByIdPort.getBudgetById(budgetId)).willReturn(Optional.of(budget));
    given(
            isUniqueNameInProjectPort.isUniqueNameInProjectByBudgetId(
                command.getBudgetId(), command.getName()))
        .willReturn(true);
    given(
            isUniqueImportKeyInProjectPort.isUniqueImportKeyInProjectByBudgetId(
                command.getBudgetId(), command.getImportKey()))
        .willReturn(false);

    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> updateBudgetService.updateBudget(command));
  }
}
