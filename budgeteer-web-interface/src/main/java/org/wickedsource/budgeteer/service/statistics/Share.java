package org.wickedsource.budgeteer.service.statistics;

import org.joda.money.Money;

public class Share {

    private String name;

    private Money share;

    public Share(Money share, String name) {
        this.share = share;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Money getShare() {
        return share;
    }
}
