package org.wickedsource.budgeteer.web.pages.imports.fileimport;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

public class ImportFilesPageTest extends AbstractWebTestTemplate {

    @Test
    void test() {
        WicketTester tester = getTester();
        ImportFilesPage page = new ImportFilesPage(DashboardPage.class, new PageParameters());
        tester.startPage(page);
        tester.assertRenderedPage(ImportFilesPage.class);
    }

    @Override
    protected void setupTest() {

    }
}
