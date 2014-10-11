package org.wickedsource.budgeteer.service.statistics;

import java.util.ArrayList;
import java.util.List;

public class BudgeteerSeries {

    private String name;

    private List<Double> values = new ArrayList<Double>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }
}
