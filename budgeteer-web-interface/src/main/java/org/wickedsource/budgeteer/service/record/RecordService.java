package org.wickedsource.budgeteer.service.record;

import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.ListUtil;
import org.wickedsource.budgeteer.persistence.record.*;
import org.wickedsource.budgeteer.service.UnknownEntityException;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RecordService {

    private final WorkRecordRepository workRecordRepository;

    private final PlanRecordRepository planRecordRepository;

    private final RecordJoiner recordJoiner;

    private final WorkRecordMapper recordMapper;

    @Autowired
    public RecordService(WorkRecordRepository workRecordRepository, PlanRecordRepository planRecordRepository, RecordJoiner recordJoiner, WorkRecordMapper recordMapper) {
        this.workRecordRepository = workRecordRepository;
        this.planRecordRepository = planRecordRepository;
        this.recordJoiner = recordJoiner;
        this.recordMapper = recordMapper;
    }

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
     * Loads the actual budget burned in the given budget and the budget planned for all people active in this budget aggregated by week with taxes.
     * @param budgetId ID of the budget whose target and actual records to load
     * @return one record for each week from the current week to the first week that was booked in the given budget
     */
    public List<AggregatedRecord> getWeeklyAggregationForBudgetWithTax(long budgetId) {
        List<WeeklyAggregatedRecordWithTaxBean> planRecords = planRecordRepository.aggregateByWeekAndBudgetWithTax(budgetId);
        List<WeeklyAggregatedRecordWithTaxBean> workRecords = workRecordRepository.aggregateByWeekAndBudgetWithTax(budgetId);
        return recordJoiner.joinWeeklyWithTax(workRecords, planRecords);
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
     * Loads the actual budget burned in the given budget and the budget planned for all people active in this budget aggregated by month with taxes.
     *
     * @param budgetId ID of the budget whose target and actual records to load
     * @return one record for each month from the current month to the first month that was booked in the given budget.
     */
    public List<AggregatedRecord> getMonthlyAggregationForBudgetWithTax(long budgetId) {
        List<MonthlyAggregatedRecordWithTaxBean> planRecords = planRecordRepository.aggregateByMonthAndBudgetWithTax(budgetId);
        List<MonthlyAggregatedRecordWithTaxBean> workRecords = workRecordRepository.aggregateByMonthAndBudgetWithTax(budgetId);
        return recordJoiner.joinMonthlyWithTax(workRecords, planRecords);
    }

    /**
     * Loads the actual budget burned in a set of given budgets and the budget planned for all people active in these budgets aggregated by week.
     *
     * @param budgetFilter filter that identifies the set of budgets whose data to load
     * @return one record for each week from the current week to the first week that was booked in the given budget
     */
    public List<AggregatedRecord> getWeeklyAggregationForBudgets(BudgetTagFilter budgetFilter) {
        List<WeeklyAggregatedRecordBean> planRecords;
        List<WeeklyAggregatedRecordBean> workRecords;

        if (budgetFilter.getSelectedTags().isEmpty()) {
            planRecords = planRecordRepository.aggregateByWeek(budgetFilter.getProjectId());
            workRecords = workRecordRepository.aggregateByWeek(budgetFilter.getProjectId());
        } else {
            planRecords = planRecordRepository.aggregateByWeekAndBudgetTags(budgetFilter.getProjectId(), budgetFilter.getSelectedTags());
            workRecords = workRecordRepository.aggregateByWeekAndBudgetTags(budgetFilter.getProjectId(), budgetFilter.getSelectedTags());
        }

        return recordJoiner.joinWeekly(workRecords, planRecords);
    }

    /**
     * Loads the actual budget burned in a set of given budgets and the budget planned for all people active in these budgets aggregated by week with taxes.
     *
     * @param budgetFilter filter that identifies the set of budgets whose data to load
     * @return one record for each week from the current week to the first week that was booked in the given budget
     */
    public List<AggregatedRecord> getWeeklyAggregationForBudgetsWithTaxes(BudgetTagFilter budgetFilter)
    {
        List<WeeklyAggregatedRecordWithTaxBean> planRecords;
        List<WeeklyAggregatedRecordWithTaxBean> workRecords;
        if(budgetFilter.getSelectedTags().isEmpty())
        {
            planRecords = planRecordRepository.aggregateByWeekWithTax(budgetFilter.getProjectId());
            workRecords = workRecordRepository.aggregateByWeekWithTax(budgetFilter.getProjectId());
        }
        else {
            planRecords = planRecordRepository.aggregateByWeekAndBudgetTagsWithTax(budgetFilter.getProjectId(), budgetFilter.getSelectedTags());
            workRecords= workRecordRepository.aggregateByWeekAndBudgetTagsWithTax(budgetFilter.getProjectId(), budgetFilter.getSelectedTags());
        }

        return recordJoiner.joinWeeklyWithTax(workRecords, planRecords);
    }

    /**
     * Loads the actual budget burned in a set of given budgets and the budget planned for all people active in these budget aggregated by month.
     *
     * @param budgetFilter filter that identifies the set of budgets whose data to load
     * @return one record for each month from the current month to the first month that was booked in the given budget.
     */
    public List<AggregatedRecord> getMonthlyAggregationForBudgets(BudgetTagFilter budgetFilter) {
        List<MonthlyAggregatedRecordBean> planRecords;
        List<MonthlyAggregatedRecordBean> workRecords;
        if (budgetFilter.getSelectedTags().isEmpty()) {
            workRecords = workRecordRepository.aggregateByMonth(budgetFilter.getProjectId());
            planRecords = planRecordRepository.aggregateByMonth(budgetFilter.getProjectId());
        } else {
            workRecords = workRecordRepository.aggregateByMonthAndBudgetTags(budgetFilter.getProjectId(), budgetFilter.getSelectedTags());
            planRecords = planRecordRepository.aggregateByMonthAndBudgetTags(budgetFilter.getProjectId(), budgetFilter.getSelectedTags());
        }
        return recordJoiner.joinMonthly(workRecords, planRecords);
    }

    /**
     * Loads the actual budget burned in a set of given budgets and the budget planned for all people active in these budget aggregated by month with taxes.
     *
     * @param budgetFilter filter that identifies the set of budgets whose data to load
     * @return one record for each month from the current month to the first month that was booked in the given budget.
     */
    public List<AggregatedRecord> getMonthlyAggregationForBudgetsWithTax(BudgetTagFilter budgetFilter) {
        List<MonthlyAggregatedRecordWithTaxBean> planRecords;
        List<MonthlyAggregatedRecordWithTaxBean> workRecords;
        if (budgetFilter.getSelectedTags().isEmpty()) {
            workRecords = workRecordRepository.aggregateByMonthWithTax(budgetFilter.getProjectId());
            planRecords = planRecordRepository.aggregateByMonthWithTax(budgetFilter.getProjectId());
        } else {
            workRecords = workRecordRepository.aggregateByMonthAndBudgetTagsWithTax(budgetFilter.getProjectId(), budgetFilter.getSelectedTags());
            planRecords = planRecordRepository.aggregateByMonthAndBudgetTagsWithTax(budgetFilter.getProjectId(), budgetFilter.getSelectedTags());
        }
        return recordJoiner.joinMonthlyWithTax(workRecords, planRecords);
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

    public void saveDailyRateForWorkRecord(WorkRecord record){
        Optional<WorkRecordEntity> entity = workRecordRepository.findById(record.getId());
        if(entity.isPresent()) {
            entity.get().setDailyRate(record.getDailyRate());
            entity.get().setEditedManually(record.isEditedManually());
            workRecordRepository.save(entity.get());
        }else{
            throw new UnknownEntityException(WorkRecordEntity.class, record.getId());
        }
    }
}
