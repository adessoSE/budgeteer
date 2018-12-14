package de.adesso.budgeteer.web.components.burntable.filter;

public enum BurnTableSortColumn {
    BUDGET("Budget"),
    NAME("Name"),
    DAILY_RATE("Daily Rate"),
    DATE("Date"),
    HOURS("Hours"),
    BUDGET_BURNED("Budget Burned");

    private final String type;

    BurnTableSortColumn(String s) {
        type = s;
    }

    public String toString() {
        return this.type;
    }
}
