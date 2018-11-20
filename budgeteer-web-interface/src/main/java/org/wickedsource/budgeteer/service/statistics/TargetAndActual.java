package org.wickedsource.budgeteer.service.statistics;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TargetAndActual {

    private List<MoneySeries> actualSeries = new ArrayList<>();
    private MoneySeries targetSeries;

    /**
     * @return the size of the target series
     */
    public int getTargetSize() {
        return targetSeries.getSize();
    }

    /**
     * @return the size of the actual series
     */
    public int getActualSize() {
        if (actualSeries.size() > 0) {
            return actualSeries.get(0).getSize();
        }
        return 0;
    }
}
