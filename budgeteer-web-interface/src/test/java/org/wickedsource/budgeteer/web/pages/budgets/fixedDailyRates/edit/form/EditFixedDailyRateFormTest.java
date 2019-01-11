package org.wickedsource.budgeteer.web.pages.budgets.fixedDailyRates.edit.form;

import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRate;
import org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRateService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.budgets.fixedDailyRates.FixedDailyRatesPage;
import org.wickedsource.budgeteer.web.pages.budgets.fixedDailyRates.edit.EditFixedDailyRatesPage;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EditFixedDailyRateFormTest extends AbstractWebTestTemplate {

    @Autowired
    private FixedDailyRateService fixedDailyRateServiceMock;
    private WicketTester tester;

    @Test
    void render() {
        tester.assertRenderedPage(EditFixedDailyRatesPage.class);
    }

    @Test
    void testSubmitEmptyMoneyAmount() {
        FormTester formTester = tester.newFormTester("form", false);
        fillDateRange(formTester);
        fillTitle(formTester);
        fillDescription(formTester);

        formTester.submit();
        tester.assertErrorMessages("The daily net rate amount may not be empty!");
        assertThat(tester.getFeedbackMessages(null)).hasSize(1);
    }

    @Test
    void testSubmitValidInput() {
        when(fixedDailyRateServiceMock.saveFixedDailyRate(any())).thenReturn(0L);
        when(fixedDailyRateServiceMock.loadFixedDailyRate(anyLong())).thenReturn(new FixedDailyRate(1));
        FormTester formTester = tester.newFormTester("form", false);
        fillDateRange(formTester);
        fillTitle(formTester);
        fillAmount(formTester);
        fillDescription(formTester);
        formTester.submit();
        assertThat(tester.getFeedbackMessages(IFeedbackMessageFilter.ALL)).hasSize(1);
        verify(fixedDailyRateServiceMock, times(1)).saveFixedDailyRate(any());
    }

    private void fillTitle(FormTester formTester) {
        formTester.setValue("title", "Test");
    }

    private void fillDescription(FormTester formTester) {
        formTester.setValue("description", "a test rate");
    }

    private void fillAmount(FormTester formTester) {
        formTester.setValue("total", "100.00");
    }

    private void fillDateRange(FormTester formTester) {
        formTester.setValue("dateRangeField", "01.01.2018 - 02.01.2018");
    }

    @Override
    protected void setupTest() {
        PageParameters parameters = new PageParameters();
        parameters.add("id", 1L);
        tester = getTester();
        tester.startPage(new EditFixedDailyRatesPage(FixedDailyRatesPage.class, parameters));
        Mockito.reset(fixedDailyRateServiceMock);
    }
}
