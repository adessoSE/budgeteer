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

    @Autowired
    BudgetService service;

    @Test
    void testRender() {
        WicketTester tester = getTester();
        when(service.loadBudgetUnits(1L)).thenReturn(createBudgetUnits());
        BudgetUnitModel model = new BudgetUnitModel(1L);
        BudgetUnitChoice dropdown = new BudgetUnitChoice("budgetUnit", model);
        tester.startComponentInPage(dropdown);
    }

    private List<Double> createBudgetUnits() {
        List<Double> units = new ArrayList<Double>();
        units.add(1d);
        units.add(500d);
        return units;
    }

    @Override
    protected void setupTest() {

    }
}
