package org.wickedsource.budgeteer.web.pages.budgets.fixedDailyRates.edit;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.budget.EditBudgetData;
import org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRate;
import org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRateService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.budgets.fixedDailyRates.FixedDailyRatesPage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EditFixedDailyRatesPageTest extends AbstractWebTestTemplate {
    @Autowired
    private FixedDailyRateService fixedDailyRateServiceMock;

    @Override
    protected void setupTest() {
        PageParameters backlinkParams = new PageParameters();
        backlinkParams.add("id", 1L);
        EditFixedDailyRatesPage page = new EditFixedDailyRatesPage(FixedDailyRatesPage.class,backlinkParams);
        getTester().startPage(page);
        Mockito.reset(fixedDailyRateServiceMock);
    }

    @Test
    void renders() {
        getTester().assertRenderedPage(EditFixedDailyRatesPage.class);
    }

    @Test
    void testRedirectOnId0() {
        WicketTester tester = getTester();

        PageParameters backlinkParams = new PageParameters();
        backlinkParams.add("id", 1L);
        EditFixedDailyRatesPage page = new EditFixedDailyRatesPage(FixedDailyRatesPage.class, backlinkParams);
        tester.startPage(page);

        tester.assertRenderedPage(EditFixedDailyRatesPage.class);
        tester.assertLabel("form:submitButtonLabel", "Create Fixed Daily Rate");
    }

    @Test
    void backlinkWorks() {
        getTester().clickLink("cancelButton1");
        getTester().assertRenderedPage(FixedDailyRatesPage.class);
    }

    @Test
    void secondBacklinkWorks() {
        getTester().clickLink("form:cancelButton2");
        getTester().assertRenderedPage(FixedDailyRatesPage.class);
    }

    @Test
    void onSubmitCallsBudgetService() {
        when(fixedDailyRateServiceMock.saveFixedDailyRate(any())).thenReturn(0L);
        when(fixedDailyRateServiceMock.loadFixedDailyRate(anyLong())).thenReturn(new FixedDailyRate(1));
        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("title","TestRate");
        formTester.setValue("description","a test Rate");
        formTester.setValue("total","100");
        formTester.setValue("dateRangeField","01.01.2018 - 02.01.2018");
        formTester.submit();

        getTester().dumpPage();
        verify(fixedDailyRateServiceMock, times(1)).saveFixedDailyRate(any());
    }

    @Test
    void onInvalidInputErrorMessagesAreShown() {
        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("title","TestRate");
        formTester.setValue("description","a test Rate");

        formTester.setValue("dateRangeField","01.01.2018 - 02.01.2018");
        formTester.submit();

        Assertions.assertThat(getTester().getFeedbackMessages(null)).hasSize(1);
    }
}
