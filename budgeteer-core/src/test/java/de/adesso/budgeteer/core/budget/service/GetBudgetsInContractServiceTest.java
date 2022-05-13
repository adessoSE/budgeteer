package de.adesso.budgeteer.core.budget.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import de.adesso.budgeteer.core.budget.domain.Budget;
import de.adesso.budgeteer.core.budget.port.out.GetBudgetsInContractPort;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetBudgetsInContractServiceTest {

  @Mock private GetBudgetsInContractPort getBudgetsInContractPort;
  @InjectMocks private GetBudgetsInContractService getBudgetsInContractService;

  @Test
  void shouldReturnAllBudgetsInContract() {
    var contractId = 1L;
    var expected =
        List.of(
            new Budget(
                2L,
                contractId,
                "name1",
                "contractName1",
                "description1",
                "importKey1",
                null,
                null,
                null,
                null,
                null,
                null,
                new Date(),
                new ArrayList<>()),
            new Budget(
                4L,
                contractId,
                "name2",
                "contractName2",
                "description2",
                "importKey2",
                null,
                null,
                null,
                null,
                null,
                null,
                new Date(),
                new ArrayList<>()));
    given(getBudgetsInContractPort.getBudgetsInContract(contractId)).willReturn(expected);

    var result = getBudgetsInContractService.getBudgetsInContract(contractId);

    assertThat(result).isEqualTo(expected);
  }

  @Test
  void shouldReturnEmptyListIfContractHasNoBudgets() {
    var contractId = 1L;
    given(getBudgetsInContractPort.getBudgetsInContract(contractId)).willReturn(List.of());

    var result = getBudgetsInContractService.getBudgetsInContract(contractId);

    assertThat(result).isEmpty();
  }
}
