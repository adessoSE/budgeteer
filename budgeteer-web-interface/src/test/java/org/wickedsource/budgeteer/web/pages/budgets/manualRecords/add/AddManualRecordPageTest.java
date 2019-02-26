package org.wickedsource.budgeteer.web.pages.budgets.manualRecords.add;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.manualRecord.ManualRecord;
import org.wickedsource.budgeteer.service.manualRecord.ManualRecordService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddManualRecordPageTest extends AbstractWebTestTemplate {
    @Autowired
    private ManualRecordService manualRecordService;

    @Override
    protected void setupTest() {
        PageParameters backlinkParams = new PageParameters();
        backlinkParams.add("id", 1L);
        AddManualRecordPage page = new AddManualRecordPage(AddManualRecordPage.class, backlinkParams);
        getTester().startPage(page);
        Mockito.reset(manualRecordService);
    }

    @Test
    void renders() {
        getTester().assertRenderedPage(AddManualRecordPage.class);
    }

    @Test
    void testRedirectOnId0() {
        WicketTester tester = getTester();

        PageParameters backlinkParams = new PageParameters();
        backlinkParams.add("id", 1L);
        AddManualRecordPage page = new AddManualRecordPage(AddManualRecordPage.class, backlinkParams);
        tester.startPage(page);

        tester.assertRenderedPage(AddManualRecordPage.class);
        tester.assertLabel("form:submitButtonLabel", "Create Manual Record");
    }

    @Test
    void submitSucessfully() {
        when(manualRecordService.saveManualRecord(any())).thenReturn(0L);
        when(manualRecordService.loadManualRecord(anyLong())).thenReturn(new ManualRecord(1));
        FormTester formTester = getTester().newFormTester("form");

        formTester.setValue("description", "a test");
        formTester.setValue("total", "100.00");
        formTester.setValue("billingDate", "02.01.2018");

        formTester.submit();

        getTester().dumpPage();
        verify(manualRecordService, times(1)).saveManualRecord(any());
    }
}