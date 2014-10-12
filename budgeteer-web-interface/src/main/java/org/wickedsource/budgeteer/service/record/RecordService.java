package org.wickedsource.budgeteer.service.record;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class RecordService {

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

    /**
     * Loads the records from the database that match the given filter. If a filter criterion is left empty (null) it
     * will not be applied.
     *
     * @param filter the filter to apply when loading records.
     * @return filtered list of records.
     */
    public List<SingleRecord> getFilteredRecords(RecordFilter filter) {
        int size = 50;
        if (filter.getPerson() != null) {
            size -= 5;
        }
        if (filter.getBudget() != null) {
            size -= 5;
        }
        if (filter.getDateRange() != null) {
            size -= 5;
        }

        List<SingleRecord> records = new ArrayList<SingleRecord>();
        for (int i = 0; i < size; i++) {
            SingleRecord record = new SingleRecord();
            record.setHours(8d);
            record.setBudgetBurned(500d);
            record.setBudgetName("Budget 1");
            record.setDailyRate(500d);
            record.setDate(new Date());
            record.setPersonName("Tom");
            records.add(record);
        }
        return records;
    }

}
