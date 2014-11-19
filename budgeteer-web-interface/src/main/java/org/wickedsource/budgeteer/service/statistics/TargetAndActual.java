package org.wickedsource.budgeteer.service.statistics;

import java.util.ArrayList;
import java.util.List;

public class TargetAndActual {

    private List<MoneySeries> actualSeries = new ArrayList<MoneySeries>();

    private MoneySeries targetSeries;

    public List<MoneySeries> getActualSeries() {
        return actualSeries;
    }

    public void setActualSeries(List<MoneySeries> actualSeries) {
        this.actualSeries = actualSeries;
    }

    public MoneySeries getTargetSeries() {
        return targetSeries;
    }

    public void setTargetSeries(MoneySeries targetSeries) {
        this.targetSeries = targetSeries;
    }
}
