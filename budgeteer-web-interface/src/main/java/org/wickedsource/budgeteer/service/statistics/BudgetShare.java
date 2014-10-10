package org.wickedsource.budgeteer.service.statistics;

public class BudgetShare {

    private String budgetName;

    private Double share;

    public BudgetShare(Double share, String budgetName) {
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
