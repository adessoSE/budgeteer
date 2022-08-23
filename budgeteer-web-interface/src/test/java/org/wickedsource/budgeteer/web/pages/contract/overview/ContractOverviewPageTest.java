package org.wickedsource.budgeteer.web.pages.contract.overview;

import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.contract.overview.table.ContractOverviewTableModel;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class ContractOverviewPageTest extends AbstractWebTestTemplate {

    @Test
    void testRender() {
        when(contractServiceMock.getContractOverviewByProject(anyLong())).thenReturn(new ContractOverviewTableModel());
        ContractOverviewPage page = new ContractOverviewPage();
        getTester().startPage(page);
        getTester().assertRenderedPage(ContractOverviewPage.class);
    }

    @Override
    protected void setupTest() {
    }
}
