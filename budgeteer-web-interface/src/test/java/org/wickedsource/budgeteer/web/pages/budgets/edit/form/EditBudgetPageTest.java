package org.wickedsource.budgeteer.web.pages.budgets.edit.form;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.budget.EditBudgetData;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.budgets.edit.EditBudgetPage;
import org.wickedsource.budgeteer.web.pages.budgets.overview.BudgetsOverviewPage;

public class EditBudgetPageTest extends AbstractWebTestTemplate {

  @Autowired private BudgetService budgetServiceMock;

  @Override
  protected void setupTest() {
    PageParameters backlinkParams = new PageParameters();
    EditBudgetPage page = new EditBudgetPage(BudgetsOverviewPage.class, backlinkParams);
    getTester().startPage(page);
    Mockito.reset(budgetServiceMock);
  }

  @Test
  void renders() {
    getTester().assertRenderedPage(EditBudgetPage.class);
  }

  @Test
  void testRedirectOnId0() {
    WicketTester tester = getTester();
    tester.startPage(EditBudgetPage.class, new PageParameters());
    tester.assertRenderedPage(EditBudgetPage.class);
    tester.assertLabel("form:submitButtonLabel", "Create Budget");
  }

  @Test
  void backlinkWorks() {
    getTester().clickLink("cancelButton1");
    getTester().assertRenderedPage(BudgetsOverviewPage.class);
  }

  @Test
  void secondBacklinkWorks() {
    getTester().clickLink("form:cancelButton2");
    getTester().assertRenderedPage(BudgetsOverviewPage.class);
  }

  @Test
  void onSubmitCallsBudgetService() {
    when(budgetServiceMock.saveBudget(any())).thenReturn(0L);
    when(budgetServiceMock.loadBudgetToEdit(anyLong())).thenReturn(new EditBudgetData(1));
    FormTester formTester = getTester().newFormTester("form");
    formTester.setValue("name", "TestBudget");
    formTester.setValue("importKey", "einImportKey"); // import key
    formTester.setValue("total", "100");
    formTester.submit();

    getTester().dumpPage();
    verify(budgetServiceMock, times(1)).saveBudget(any());
  }

  @Test
  void onInvalidInputErrorMessagesAreShown() {
    FormTester formTester = getTester().newFormTester("form");
    formTester.setValue("name", "TestBudget");
    formTester.setValue("importKey", "einImportKey"); // import key
    formTester.submit();

    Assertions.assertThat(getTester().getFeedbackMessages(null)).hasSize(1);
  }
}
