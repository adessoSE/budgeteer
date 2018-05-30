package org.wickedsource.budgeteer.web.pages.template;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.templates.TemplatesPage;
import org.wickedsource.budgeteer.web.pages.templates.templateimport.ImportTemplatesPage;

public class TemplatesPageTest extends AbstractWebTestTemplate {



    @Test
    void testRender() {
        WicketTester tester = getTester();
        tester.startPage(TemplatesPage.class);
        tester.assertRenderedPage(TemplatesPage.class);
    }

    @Test
    void testImportClick() {
        WicketTester tester = getTester();
        tester.startPage(TemplatesPage.class);
        tester.clickLink("importLink");
        tester.assertRenderedPage(ImportTemplatesPage.class);
    }

    @Override
    public void setupTest(){

    }
}
