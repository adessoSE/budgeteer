package org.wickedsource.budgeteer.service.record;

import java.util.Comparator;

public class AggregatedRecordComparator implements Comparator<AggregatedRecord> {

    @Override
    public int compare(AggregatedRecord o1, AggregatedRecord o2) {
        return o1.getAggregationPeriodStart().compareTo(o2.getAggregationPeriodStart());
    }
}
