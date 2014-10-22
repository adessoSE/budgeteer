package org.wickedsource.budgeteer.service.statistics;

import org.joda.money.Money;

import java.util.ArrayList;
import java.util.List;

public class BudgeteerSeries {

    private String name;

    private List<Money> values = new ArrayList<Money>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Money> getValues() {
        return values;
    }

    public void setValues(List<Money> values) {
        this.values = values;
    }

}
