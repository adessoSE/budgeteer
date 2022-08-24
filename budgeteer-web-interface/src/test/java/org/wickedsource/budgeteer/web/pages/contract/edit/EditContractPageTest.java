package org.wickedsource.budgeteer.web.pages.contract.edit;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

class EditContractPageTest extends AbstractWebTestTemplate {

  @Override
  protected void setupTest() {}

  @Test
  void testCreateContractOnId0() {
    WicketTester tester = getTester();
    tester.startPage(EditContractPage.class, new PageParameters());
    tester.assertRenderedPage(EditContractPage.class);
    tester.assertLabel("pageTitle", "Create Contract");
  }
}
