package org.wickedsource.budgeteer.service.statistics;

import org.joda.money.Money;

/**
 * A numeric value that belongs to a certain budget.
 */
public class BudgetValue {

    private String budgetName;

    private Money share;

    public BudgetValue(Money share, String budgetName) {
        this.share = share;
        this.budgetName = budgetName;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public Money getShare() {
        return share;
    }
}
