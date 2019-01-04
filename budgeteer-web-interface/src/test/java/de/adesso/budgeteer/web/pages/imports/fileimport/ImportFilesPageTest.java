package de.adesso.budgeteer.web.pages.imports.fileimport;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import de.adesso.budgeteer.web.pages.dashboard.DashboardPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

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
