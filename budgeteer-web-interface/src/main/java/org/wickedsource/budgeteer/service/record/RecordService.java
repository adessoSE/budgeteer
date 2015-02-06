package org.wickedsource.budgeteer.service.record;

import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.ListUtil;
import org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordBean;
import org.wickedsource.budgeteer.persistence.record.PlanRecordRepository;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RecordService {

    @Autowired
    private WorkRecordRepository workRecordRepository;

    @Autowired
    private PlanRecordRepository planRecordRepository;

    @Autowired
    private RecordJoiner recordJoiner;

    @Autowired
    private WorkRecordMapper recordMapper;

    /**
     * Loads the actual budget burned by the given person and the budget planned for this person aggregated by week.
     *
     * @param personId ID of the person whose target and actual records to load
     * @return one record for each week from the current week to the first week that person booked hours
     */
    public List<AggregatedRecord> getWeeklyAggregationForPerson(long personId) {
        List<WeeklyAggregatedRecordBean> planRecords = planRecordRepository.aggregateByWeekAndPerson(personId);
        List<WeeklyAggregatedRecordBean> workRecords = workRecordRepository.aggregateByWeekAndPerson(personId);
        return recordJoiner.joinWeekly(workRecords, planRecords);
    }


    /**
     * Loads the actual budget burned by the given person and the budget planned for this person aggregated by month.
     *
     * @param personId ID of the person whose target and actual records to load
     * @return one record for each month from the current month to the first month that person booked hours
     */
    public List<AggregatedRecord> getMonthlyAggregationForPerson(long personId) {
        List<MonthlyAggregatedRecordBean> planRecords = planRecordRepository.aggregateByMonthAndPerson(personId);
        List<MonthlyAggregatedRecordBean> workRecords = workRecordRepository.aggregateByMonthAndPerson(personId);
        return recordJoiner.joinMonthly(workRecords, planRecords);
    }

    /**
     * Loads the actual budget burned in the given budget and the budget planned for all people active in this budget aggregated by week.
     *
     * @param budgetId ID of the budget whose target and actual records to load
     * @return one record for each week from the current week to the first week that was booked in the given budget
     */
    public List<AggregatedRecord> getWeeklyAggregationForBudget(long budgetId) {
        List<WeeklyAggregatedRecordBean> planRecords = planRecordRepository.aggregateByWeekAndBudget(budgetId);
        List<WeeklyAggregatedRecordBean> workRecords = workRecordRepository.aggregateByWeekAndBudget(budgetId);
        return recordJoiner.joinWeekly(workRecords, planRecords);
    }

    /**
     * Loads the actual budget burned in the given budget and the budget planned for all people active in this budget aggregated by month.
     *
     * @param budgetId ID of the budget whose target and actual records to load
     * @return one record for each month from the current month to the first month that was booked in the given budget.
     */
    public List<AggregatedRecord> getMonthlyAggregationForBudget(long budgetId) {
        List<MonthlyAggregatedRecordBean> planRecords = planRecordRepository.aggregateByMonthAndBudget(budgetId);
        List<MonthlyAggregatedRecordBean> workRecords = workRecordRepository.aggregateByMonthAndBudget(budgetId);
        return recordJoiner.joinMonthly(workRecords, planRecords);
    }

    /**
     * Loads the actual budget burned in a set of given budgets and the budget planned for all people active in these budgets aggregated by week.
     *
     * @param budgetFilter filter that identifies the set of budgets whose data to load
     * @return one record for each week from the current week to the first week that was booked in the given budget
     */
    public List<AggregatedRecord> getWeeklyAggregationForBudgets(BudgetTagFilter budgetFilter) {
        List<WeeklyAggregatedRecordBean> planRecords = new ArrayList<WeeklyAggregatedRecordBean>();
        List<WeeklyAggregatedRecordBean> workRecords = new ArrayList<WeeklyAggregatedRecordBean>();
        if (budgetFilter.getSelectedTags().isEmpty()) {
            planRecords = planRecordRepository.aggregateByWeek(budgetFilter.getProjectId());
            workRecords = workRecordRepository.aggregateByWeek(budgetFilter.getProjectId());
        }else{
            planRecords = planRecordRepository.aggregateByWeekAndBudgetTags(budgetFilter.getProjectId(), budgetFilter.getSelectedTags());
            workRecords = workRecordRepository.aggregateByWeekAndBudgetTags(budgetFilter.getProjectId(), budgetFilter.getSelectedTags());
        }
        return recordJoiner.joinWeekly(workRecords, planRecords);
    }

    /**
     * Loads the actual budget burned in a set of given budgets and the budget planned for all people active in these budget aggregated by month.
     *
     * @param budgetFilter filter that identifies the set of budgets whose data to load
     * @return one record for each month from the current month to the first month that was booked in the given budget.
     */
    public List<AggregatedRecord> getMonthlyAggregationForBudgets(BudgetTagFilter budgetFilter) {
        List<MonthlyAggregatedRecordBean> planRecords = new ArrayList<MonthlyAggregatedRecordBean>();
        List<MonthlyAggregatedRecordBean> workRecords = new ArrayList<MonthlyAggregatedRecordBean>();
        if (budgetFilter.getSelectedTags().isEmpty()) {
            workRecords = workRecordRepository.aggregateByMonth(budgetFilter.getProjectId());
            planRecords = planRecordRepository.aggregateByMonth(budgetFilter.getProjectId());
        }else{
            workRecords = workRecordRepository.aggregateByMonthAndBudgetTags(budgetFilter.getProjectId(), budgetFilter.getSelectedTags());
            planRecords = planRecordRepository.aggregateByMonthAndBudgetTags(budgetFilter.getProjectId(), budgetFilter.getSelectedTags());
        }
        return recordJoiner.joinMonthly(workRecords, planRecords);
    }

    /**
     * Loads the records from the database that match the given filter. If a filter criterion is left empty (null) it
     * will not be applied.
     *
     * @param filter the filter to apply when loading records.
     * @return filtered list of records.
     */
    public List<WorkRecord> getFilteredRecords(WorkRecordFilter filter) {
        Predicate query = WorkRecordQueries.findByFilter(filter);
        return recordMapper.map(ListUtil.toArrayList(workRecordRepository.findAll(query)));
    }

}
