package org.wickedsource.budgeteer.service.settings;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingsService {

    /**
     * Returns the units in which the user can have his budget values displayed. One unit is active, the others are
     * inactive.
     *
     * @param userId id of the user for whom to liad the budget units.
     * @return a list containing all available budget units, one of which is currently active.
     */
    public List<BudgetUnit> getBudgetUnits(long userId) {
        return createBudgetUnits();
    }

    /**
     * Activates the selected budget unit for the given user and deactivates all others.
     *
     * @param userId id of the user whose active budget unit to change
     * @param unit   the budget unit that should be activated
     */
    public void activateBudgetUnit(long userId, BudgetUnit unit) {

    }

    private List<BudgetUnit> createBudgetUnits() {
        List<BudgetUnit> units = new ArrayList<BudgetUnit>();
        units.add(new BudgetUnit("Unit 1", true));
        units.add(new BudgetUnit("Unit 2", false));
        return units;
    }

}
