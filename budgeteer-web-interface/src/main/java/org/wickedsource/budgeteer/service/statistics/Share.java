package org.wickedsource.budgeteer.service.statistics;

import org.joda.money.Money;

public class Share {

    private String budgetName;

    private Money share;

    public Share(Money share, String budgetName) {
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
