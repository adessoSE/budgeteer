package org.wickedsource.budgeteer.web.pages.budgets.edit.form;

import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.budgets.edit.EditBudgetPage;
import org.wickedsource.budgeteer.web.pages.budgets.overview.BudgetsOverviewPage;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EditBudgetFormTest extends AbstractWebTestTemplate {

    @Autowired
    private BudgetService budgetServiceMock;
    private WicketTester tester;

    @Test
    void render() {
        tester.assertRenderedPage(EditBudgetPage.class);
    }

    @Test
    void testSubmitEmptyBudget() {
        FormTester formTester = tester.newFormTester("form", false);
        fillName(formTester);
        fillImportKey(formTester);
        formTester.submit();
        tester.assertErrorMessages("The budget total may not be empty!");
        assertThat(tester.getFeedbackMessages(null)).hasSize(1);
    }

    @Test
    void testSubmitEmptyName() {
        FormTester formTester = tester.newFormTester("form", false);
        fillImportKey(formTester);
        fillBudget(formTester);
        formTester.submit();
        tester.assertErrorMessages("The title of the budget may not be empty!");
        assertThat(tester.getFeedbackMessages(null)).hasSize(1);
    }

    @Test
    void testSubmitEmptyImportKey() {
        FormTester formTester = tester.newFormTester("form", false);
        fillName(formTester);
        fillBudget(formTester);
        formTester.submit();
        tester.assertErrorMessages("The import key may not be empty!");
        assertThat(tester.getFeedbackMessages(null)).hasSize(1);
    }

    @Test
    void testSubmitValidInput() {
        FormTester formTester = tester.newFormTester("form", false);
        fillName(formTester);
        fillImportKey(formTester);
        fillBudget(formTester);
        formTester.submit();
        assertThat(tester.getFeedbackMessages(IFeedbackMessageFilter.ALL)).hasSize(1);
        verify(budgetServiceMock, times(1)).saveBudget(any());
    }

    @Test
    void testDuplicateImportKey() {
        FormTester formTester = tester.newFormTester("form", false);
        doThrow(new DataIntegrityViolationException("contraints!")).when(budgetServiceMock).saveBudget(any());

        fillName(formTester);
        fillImportKey(formTester);
        fillBudget(formTester);
        formTester.submit();

        tester.assertErrorMessages("The import key is already used in another budget.");
        verify(budgetServiceMock, times(1)).saveBudget(any());
    }


    private void fillImportKey(FormTester formTester) {
        formTester.setValue("importKey", "importkey");
    }

    private void fillBudget(FormTester formTester) {
        formTester.setValue("total", "100.00");
    }

    private void fillName(FormTester formTester) {
        formTester.setValue("name", "budgetname");
    }

    @Override
    protected void setupTest() {
        tester = getTester();
        tester.startPage(new EditBudgetPage(BudgetsOverviewPage.class, null));
        Mockito.reset(budgetServiceMock);
    }
}
