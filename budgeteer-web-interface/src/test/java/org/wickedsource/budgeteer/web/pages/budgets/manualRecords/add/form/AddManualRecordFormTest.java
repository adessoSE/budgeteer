package org.wickedsource.budgeteer.web.pages.budgets.manualRecords.add.form;

import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.manualRecord.ManualRecord;
import org.wickedsource.budgeteer.service.manualRecord.ManualRecordService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.budgets.manualRecords.add.AddManualRecordPage;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddManualRecordFormTest extends AbstractWebTestTemplate {

    @Autowired
    private ManualRecordService serviceMock;
    private WicketTester tester;

    @Test
    void render() {
        tester.assertRenderedPage(AddManualRecordPage.class);
    }

    @Override
    protected void setupTest() {
        PageParameters parameters = new PageParameters();
        parameters.add("id", 1L);
        tester = getTester();
        tester.startPage(new AddManualRecordPage(AddManualRecordPage.class, parameters));
        Mockito.reset(serviceMock);
    }

    @Test
    void testSubmitEmptyMoneyAmount() {
        FormTester formTester = tester.newFormTester("form", false);
        fillBillingDate(formTester);
        fillDescription(formTester);

        formTester.submit();
        tester.assertErrorMessages("The net record amount may not be empty!");
        assertThat(tester.getFeedbackMessages(null)).hasSize(1);
    }

    @Test
    void testSubmitValidInput() {
        when(serviceMock.saveManualRecord(any())).thenReturn(0L);
        when(serviceMock.loadManualRecord(anyLong())).thenReturn(new ManualRecord(1));
        FormTester formTester = tester.newFormTester("form", false);

        fillBillingDate(formTester);
        fillAmount(formTester);
        fillDescription(formTester);

        formTester.submit();

        assertThat(tester.getFeedbackMessages(IFeedbackMessageFilter.ALL)).hasSize(1);
        verify(serviceMock, times(1)).saveManualRecord(any());
    }

    private void fillDescription(FormTester formTester) {
        formTester.setValue("description", "a test");
    }

    private void fillAmount(FormTester formTester) {
        formTester.setValue("total", "100.00");
    }

    private void fillBillingDate(FormTester formTester) {
        formTester.setValue("billingDate", "02.01.2018");
    }

}
