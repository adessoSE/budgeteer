package de.adesso.budgeteer.web.pages.template;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import de.adesso.budgeteer.web.pages.templates.TemplatesPage;
import de.adesso.budgeteer.web.pages.templates.templateimport.ImportTemplatesPage;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

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
