package org.wickedsource.budgeteer.web.pages.contract.edit;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.contract.ContractBaseData;
import org.wickedsource.budgeteer.service.contract.ContractService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class EditContractPageTest extends AbstractWebTestTemplate {

  @Autowired private ContractService contractServiceMock;

  @Override
  protected void setupTest() {
    when(contractServiceMock.getContractById(anyLong())).thenReturn(new ContractBaseData());
    when(contractServiceMock.getEmptyContractModel(anyLong())).thenReturn(new ContractBaseData());
  }

  @Test
  void testCreateContractOnId0() {
    WicketTester tester = getTester();
    tester.startPage(EditContractPage.class, new PageParameters());
    tester.assertRenderedPage(EditContractPage.class);
    tester.assertLabel("pageTitle", "Create new Contract");
  }
}
