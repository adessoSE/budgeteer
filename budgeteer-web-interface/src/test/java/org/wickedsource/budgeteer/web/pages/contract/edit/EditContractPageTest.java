package org.wickedsource.budgeteer.web.pages.contract.edit;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.wickedsource.budgeteer.service.contract.ContractBaseData;
import org.wickedsource.budgeteer.service.contract.ContractService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class EditContractPageTest extends AbstractWebTestTemplate {

    @Override
    protected void setupTest() {
    }

    @Test
    void testCreateContractOnId0() {
        WicketTester tester = getTester();
        tester.startPage(EditContractPage.class, new PageParameters());
        tester.assertRenderedPage(EditContractPage.class);
        tester.assertLabel("pageTitle", "Create Contract");
    }
}
