package org.wickedsource.budgeteer.web.pages.base.basepage.budgetunitchoice;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

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
