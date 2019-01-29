package org.wickedsource.budgeteer.persistence.record;

import java.util.Comparator;

public class WeeklyAggregatedRecordBeanComparator implements Comparator<WeeklyAggregatedRecordBean> {

    @Override
    public int compare(WeeklyAggregatedRecordBean o1, WeeklyAggregatedRecordBean o2) {
        if (o1.getYear() < o2.getYear()) {
            return -1;
        } else if (o1.getYear() > o2.getYear()) {
            return 1;
        } else {
            return Integer.compare(o1.getWeek(), o2.getWeek());
        }
    }
}