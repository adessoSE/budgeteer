package org.wickedsource.budgeteer.service.hours;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class AggregationService {

    private Random random = new Random();

    /**
     * Loads the actual budget burned by the given person and the budget planned for this person aggregated by week.
     *
     * @param personId ID of the person whose target and actual records to load
     * @return one record for each week from the current week to the first week that person booked hours
     */
    public List<AggregatedRecord> getWeeklyAggregationForPerson(long personId) {
        List<AggregatedRecord> list = new ArrayList<AggregatedRecord>();
        for (int i = 0; i < 20; i++) {
            AggregatedRecord record = new AggregatedRecord();
            record.setAggregationPeriodTitle("Week #" + i);
            record.setAggregationPeriodStart(new Date());
            record.setAggregationPeriodEnd(new Date());
            record.setBudgetBurned(random.nextDouble());
            record.setBudgetPlanned(random.nextDouble());
            record.setHours(random.nextDouble());
            list.add(record);
        }
        return list;
    }

    /**
     * Loads the actual budget burned by the given person and the budget planned for this person aggregated by month.
     *
     * @param personId ID of the person whose target and actual records to load
     * @return one record for each month from the current month to the first month that person booked hours
     */
    public List<AggregatedRecord> getMonthlyAggregationForPerson(long personId) {
        List<AggregatedRecord> list = new ArrayList<AggregatedRecord>();
        for (int i = 0; i < 20; i++) {
            AggregatedRecord record = new AggregatedRecord();
            record.setAggregationPeriodTitle("2014/" + i);
            record.setAggregationPeriodStart(new Date());
            record.setAggregationPeriodEnd(new Date());
            record.setBudgetBurned(random.nextDouble());
            record.setBudgetPlanned(random.nextDouble());
            record.setHours(random.nextDouble());
            list.add(record);
        }
        return list;
    }

}
