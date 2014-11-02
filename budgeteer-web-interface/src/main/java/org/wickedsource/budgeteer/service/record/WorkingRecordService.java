package org.wickedsource.budgeteer.service.record;

import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.imports.api.WorkingRecord;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class WorkingRecordService {

    private Random random = new Random();

    /**
     * Loads the actual budget burned by the given person and the budget planned for this person aggregated by week.
     *
     * @param personId ID of the person whose target and actual records to load
     * @return one record for each week from the current week to the first week that person booked hours
     */
    public List<AggregatedWorkingRecord> getWeeklyAggregationForPerson(long personId) {
        List<AggregatedWorkingRecord> list = new ArrayList<AggregatedWorkingRecord>();
        for (int i = 0; i < 20; i++) {
            AggregatedWorkingRecord record = new AggregatedWorkingRecord();
            record.setAggregationPeriodTitle("Week #" + i);
            record.setAggregationPeriodStart(new Date());
            record.setAggregationPeriodEnd(new Date());
            record.setBudgetBurned(MoneyUtil.createMoneyFromCents(random.nextInt(100000)));
            record.setBudgetPlanned(MoneyUtil.createMoneyFromCents(random.nextInt(100000)));
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
    public List<AggregatedWorkingRecord> getMonthlyAggregationForPerson(long personId) {
        List<AggregatedWorkingRecord> list = new ArrayList<AggregatedWorkingRecord>();
        for (int i = 0; i < 20; i++) {
            AggregatedWorkingRecord record = new AggregatedWorkingRecord();
            record.setAggregationPeriodTitle("2014/" + i);
            record.setAggregationPeriodStart(new Date());
            record.setAggregationPeriodEnd(new Date());
            record.setBudgetBurned(MoneyUtil.createMoneyFromCents(random.nextInt(100000)));
            record.setBudgetPlanned(MoneyUtil.createMoneyFromCents(random.nextInt(100000)));
            record.setHours(random.nextDouble());
            list.add(record);
        }
        return list;
    }

    /**
     * Loads the actual budget burned in the given budget and the budget planned for all people active in this budget aggregated by week.
     *
     * @param budgetId ID of the budget whose target and actual records to load
     * @return one record for each week from the current week to the first week that was booked in the given budget
     */
    public List<AggregatedWorkingRecord> getWeeklyAggregationForBudget(long budgetId) {
        return getWeeklyAggregationForPerson(budgetId);
    }

    /**
     * Loads the actual budget burned in the given budget and the budget planned for all people active in this budget aggregated by month.
     *
     * @param budgetId ID of the budget whose target and actual records to load
     * @return one record for each month from the current month to the first month that was booked in the given budget.
     */
    public List<AggregatedWorkingRecord> getMonthlyAggregationForBudget(long budgetId) {
        return getMonthlyAggregationForPerson(budgetId);
    }

    /**
     * Loads the actual budget burned in a set of given budgets and the budget planned for all people active in these budgets aggregated by week.
     *
     * @param budgetFilter filter that identifies the set of budgets whose data to load
     * @return one record for each week from the current week to the first week that was booked in the given budget
     */
    public List<AggregatedWorkingRecord> getWeeklyAggregationForBudgets(BudgetTagFilter budgetFilter) {
        return getWeeklyAggregationForPerson(1l);
    }

    /**
     * Loads the actual budget burned in a set of given budgets and the budget planned for all people active in these budget aggregated by month.
     *
     * @param budgetFilter filter that identifies the set of budgets whose data to load
     * @return one record for each month from the current month to the first month that was booked in the given budget.
     */
    public List<AggregatedWorkingRecord> getMonthlyAggregationForBudgets(BudgetTagFilter budgetFilter) {
        return getMonthlyAggregationForPerson(1l);
    }

    /**
     * Loads the records from the database that match the given filter. If a filter criterion is left empty (null) it
     * will not be applied.
     *
     * @param filter the filter to apply when loading records.
     * @return filtered list of records.
     */
    public List<WorkingRecord> getFilteredRecords(WorkingRecordFilter filter) {
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

        List<WorkingRecord> records = new ArrayList<WorkingRecord>();
        for (int i = 0; i < size; i++) {
            WorkingRecord record = new WorkingRecord();
            record.setHours(8d);
            record.setBudgetBurned(MoneyUtil.createMoney(500d));
            record.setBudgetName("Budget 1");
            record.setDailyRate(MoneyUtil.createMoney(500d));
            record.setDate(new Date());
            record.setPersonName("Tom");
            records.add(record);
        }
        return records;
    }

    /**
     * Loads the records from the database that match the given filter.
     *
     * @param filter the filter to apply when loading records.
     * @return filtered list of records.
     */
    public List<WorkingRecord> getFilteredRecords(BudgetTagFilter filter) {
        int size = 50;
        for (int i = 0; i < filter.getSelectedTags().size(); i++) {
            size -= 5;
        }

        List<WorkingRecord> records = new ArrayList<WorkingRecord>();
        for (int i = 0; i < size; i++) {
            WorkingRecord record = new WorkingRecord();
            record.setHours(8d);
            record.setBudgetBurned(MoneyUtil.createMoney(500d));
            record.setBudgetName("Budget 1");
            record.setDailyRate(MoneyUtil.createMoney(500d));
            record.setDate(new Date());
            record.setPersonName("Tom");
            records.add(record);
        }

        return records;
    }

}
