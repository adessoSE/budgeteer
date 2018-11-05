package org.wickedsource.budgeteer.web.pages.contract.overview;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.contract.ContractService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.budgets.edit.EditBudgetPage;
import org.wickedsource.budgeteer.web.pages.budgets.overview.BudgetsOverviewPage;
import org.wickedsource.budgeteer.web.pages.contract.overview.ContractOverviewPage;
import org.wickedsource.budgeteer.web.pages.contract.overview.table.ContractOverviewTableModel;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class ContractOverviewPageTest extends AbstractWebTestTemplate {

    @Autowired
    private ContractService contractServiceMock;

    @Test
    void testRender() {
        ContractOverviewPage page = new ContractOverviewPage();
        getTester().startPage(page);
        getTester().assertRenderedPage(ContractOverviewPage.class);
    }

    @Override
    protected void setupTest() {
        when(contractServiceMock.getContractOverviewByProject(anyLong())).thenReturn(new ContractOverviewTableModel());
    }
}
