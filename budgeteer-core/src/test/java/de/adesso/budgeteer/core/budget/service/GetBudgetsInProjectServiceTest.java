package de.adesso.budgeteer.core.budget.service;

import de.adesso.budgeteer.core.budget.domain.Budget;
import de.adesso.budgeteer.core.budget.port.out.GetBudgetsInProjectPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GetBudgetsInProjectServiceTest {

    @Mock private GetBudgetsInProjectPort getBudgetsInProjectPort;
    @InjectMocks private GetBudgetsInProjectService getBudgetsInProjectService;

    @Test
    void shouldReturnAllBudgetsInProject() {
        var projectId = 1L;
        var expected = List.of(new Budget(
                        2L,
                        3L,
                        "name1",
                        "contractName1",
                        "description1",
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
                        5L,
                        "name2",
                        "contractName2",
                        "description2",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        new Date(),
                        new ArrayList<>()
                )
        );
        given(getBudgetsInProjectPort.getBudgetsInProject(projectId)).willReturn(expected);

        var result = getBudgetsInProjectService.getBudgetsInProject(projectId);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldReturnEmptyListIfProjectHasNoBudgets() {
        var projectId = 1L;
        given(getBudgetsInProjectPort.getBudgetsInProject(projectId)).willReturn(List.of());

        var result = getBudgetsInProjectService.getBudgetsInProject(projectId);

        assertThat(result).isEmpty();
    }

}
