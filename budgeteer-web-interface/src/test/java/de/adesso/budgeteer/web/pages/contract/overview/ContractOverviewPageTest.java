package de.adesso.budgeteer.web.pages.contract.overview;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import de.adesso.budgeteer.service.contract.ContractService;
import de.adesso.budgeteer.service.contract.ContractSortingService;
import de.adesso.budgeteer.web.AbstractWebTestTemplate;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class ContractOverviewPageTest extends AbstractWebTestTemplate {

    @Autowired
    private ContractService contractServiceMock;

    @Autowired
    private ContractSortingService contractSortingService;

    @Test
    void testRender() {
        ContractOverviewPage page = new ContractOverviewPage();
        getTester().startPage(page);
        getTester().assertRenderedPage(ContractOverviewPage.class);
    }

    @Override
    protected void setupTest() {
        when(contractSortingService.getSortedContracts(anyLong(), anyLong())).thenReturn(new ArrayList<>());
    }
}
