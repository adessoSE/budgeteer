package org.wickedsource.budgeteer.service.statistics;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TargetAndActual {

    private List<MoneySeries> actualSeries = new ArrayList<MoneySeries>();
    private MoneySeries targetSeries;
}
