package org.wickedsource.budgeteer.persistence.budget;

public class MissingBudgetTotalBean {

    private long budgetId;

    private String budgetName;

    public MissingBudgetTotalBean(long budgetId, String budgetName) {
        this.budgetId = budgetId;
        this.budgetName = budgetName;
    }

    public long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(long budgetId) {
        this.budgetId = budgetId;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }
}
