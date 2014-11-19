package org.wickedsource.budgeteer.persistence.record;

import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;

public class ShareBean {

    private String name;

    private long valueInCents;

    public ShareBean(String name, long valueInCents) {
        this.name = name;
        this.valueInCents = valueInCents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getValueInCents() {
        return valueInCents;
    }

    public Money getValue(){
        return MoneyUtil.createMoneyFromCents(valueInCents);
    }

    public void setValueInCents(long valueInCents) {
        this.valueInCents = valueInCents;
    }
}
