package de.adesso.budgeteer.web.pages.base.basepage.budgetunitchoice;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;

public class BudgetUnitChoiceTest extends AbstractWebTestTemplate {

    @Test
    void testRender() {
        WicketTester tester = getTester();
        BudgetUnitModel model = new BudgetUnitModel(1L);
        BudgetUnitChoice dropdown = new BudgetUnitChoice("budgetUnit", model);
        tester.startComponentInPage(dropdown);
    }

    @Override
    protected void setupTest() {

    }
}
