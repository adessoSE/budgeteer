package org.wickedsource.budgeteer.web.pages.budgets.manualRecords.overview;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.manualRecord.ManualRecordService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class ManualRecordOverviewPageTest extends AbstractWebTestTemplate {

    @Autowired
    private ManualRecordService serviceMock;

    @Test
    void render() {
        WicketTester tester = getTester();
        PageParameters parameters = new PageParameters();
        parameters.add("id", 1L);
        tester.startPage(ManualRecordOverviewPage.class, parameters);
        tester.assertRenderedPage(ManualRecordOverviewPage.class);
    }

    @Override
    protected void setupTest() {

    }
}
