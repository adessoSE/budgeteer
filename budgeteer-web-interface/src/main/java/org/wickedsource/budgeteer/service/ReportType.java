package org.wickedsource.budgeteer.service;

public enum ReportType {
    BUDGET_REPORT("Budget"),
    CONTRACT_REPORT("Contract");

    private final String type;

    ReportType(String s) {
        type = s;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
