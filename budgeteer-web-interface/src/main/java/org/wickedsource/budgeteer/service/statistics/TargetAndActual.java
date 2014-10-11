package org.wickedsource.budgeteer.service.statistics;

import java.util.ArrayList;
import java.util.List;

public class TargetAndActual {

    private List<BudgeteerSeries> actualSeries = new ArrayList<BudgeteerSeries>();

    private BudgeteerSeries targetSeries;

    public List<BudgeteerSeries> getActualSeries() {
        return actualSeries;
    }

    public void setActualSeries(List<BudgeteerSeries> actualSeries) {
        this.actualSeries = actualSeries;
    }

    public BudgeteerSeries getTargetSeries() {
        return targetSeries;
    }

    public void setTargetSeries(BudgeteerSeries targetSeries) {
        this.targetSeries = targetSeries;
    }
}
