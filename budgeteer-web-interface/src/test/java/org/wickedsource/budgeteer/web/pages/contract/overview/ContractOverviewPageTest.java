package org.wickedsource.budgeteer.web.pages.contract.overview;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.wickedsource.budgeteer.service.contract.ContractService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.contract.overview.table.ContractOverviewTableModel;

public class ContractOverviewPageTest extends AbstractWebTestTemplate {

  @MockBean private ContractService contractServiceMock;

  @Test
  void testRender() {
    ContractOverviewPage page = new ContractOverviewPage();
    getTester().startPage(page);
    getTester().assertRenderedPage(ContractOverviewPage.class);
  }

  @Override
  protected void setupTest() {
    when(contractServiceMock.getContractOverviewByProject(anyLong()))
        .thenReturn(new ContractOverviewTableModel());
  }
}
