package org.wickedsource.budgeteer.service.statistics;

/**
 * A numeric value that belongs to a certain budget.
 */
public class BudgetValue {

    private String budgetName;

    private Double share;

    public BudgetValue(Double share, String budgetName) {
        this.share = share;
        this.budgetName = budgetName;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public Double getShare() {
        return share;
    }
}
