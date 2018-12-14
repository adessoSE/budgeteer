package de.adesso.budgeteer.service;

public enum ReportType {
    BUDGET_REPORT("Budget"),
    CONTRACT_REPORT("Contract");

    private final String type;

    ReportType(String s) {
        type = s;
    }

    public String toString() {
        return this.type;
    }
}
