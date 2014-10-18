package org.wickedsource.budgeteer.web.pages.base.basepage.budgetunitchoice;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.settings.BudgetUnit;
import org.wickedsource.budgeteer.service.settings.SettingsService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class BudgetUnitChoiceTest extends AbstractWebTestTemplate {

    @Autowired
    SettingsService settingsService;

    @Test
    public void testRender() {
        WicketTester tester = getTester();
        when(settingsService.getBudgetUnits(1l)).thenReturn(createBudgetUnits());
        BudgetUnitModel model = new BudgetUnitModel(1l);
        BudgetUnitChoice dropdown = new BudgetUnitChoice("budgetUnit", model);
        tester.startComponentInPage(dropdown);
    }

    private List<BudgetUnit> createBudgetUnits() {
        List<BudgetUnit> units = new ArrayList<BudgetUnit>();
        units.add(new BudgetUnit("Unit 1", true));
        units.add(new BudgetUnit("Unit 2", false));
        return units;
    }
}
