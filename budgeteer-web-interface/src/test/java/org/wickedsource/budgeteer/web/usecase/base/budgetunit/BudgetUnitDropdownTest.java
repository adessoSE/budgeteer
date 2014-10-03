package org.wickedsource.budgeteer.web.usecase.base.budgetunit;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.settings.BudgetUnit;
import org.wickedsource.budgeteer.service.settings.SettingsService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.usecase.base.component.budgetunit.BudgetUnitDropdown;
import org.wickedsource.budgeteer.web.usecase.base.component.budgetunit.BudgetUnitModel;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class BudgetUnitDropdownTest extends AbstractWebTestTemplate {

    @Autowired
    SettingsService settingsService;

    @Test
    public void testRender() {
        WicketTester tester = getTester();
        when(settingsService.getBudgetUnits(1l)).thenReturn(createBudgetUnits());
        BudgetUnitModel model = new BudgetUnitModel(1l);
        BudgetUnitDropdown dropdown = new BudgetUnitDropdown("budgetUnit", model);
        tester.startComponentInPage(dropdown);
    }

    private List<BudgetUnit> createBudgetUnits() {
        List<BudgetUnit> units = new ArrayList<BudgetUnit>();
        units.add(new BudgetUnit("Unit 1", true));
        units.add(new BudgetUnit("Unit 2", false));
        return units;
    }
}
