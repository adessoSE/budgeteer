package org.wickedsource.budgeteer.service.settings;

import java.io.Serializable;

/**
 * A unit in which budget values can be expressed. Each user can choose in which unit to display his budgets, so one unit
 * can be active for a user at a time.
 */
public class BudgetUnit implements Serializable {

    private String unitTitle;

    private boolean active;

    public BudgetUnit(String unitTitle, boolean active) {
        this.unitTitle = unitTitle;
        this.active = active;
    }

    public String getUnitTitle() {
        return unitTitle;
    }

    public boolean isActive() {
        return active;
    }
}
