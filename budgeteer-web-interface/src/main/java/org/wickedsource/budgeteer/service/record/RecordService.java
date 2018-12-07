package org.wickedsource.budgeteer.service.record;

import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.ListUtil;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.record.*;
import org.wickedsource.budgeteer.persistence.manualRecord.ManualRecordRepository;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;
import org.wickedsource.budgeteer.service.statistics.MonthlyStats;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class RecordService {

    @Autowired
    private WorkRecordRepository workRecordRepository;

    @Autowired
    private PlanRecordRepository planRecordRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private RecordJoiner recordJoiner;

    @Autowired
    private WorkRecordMapper recordMapper;

    @Autowired
    private ManualRecordRepository manualRecordRepository;

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
     *
     * @param budgetId ID of the budget whose target and actual records to load
     * @return one record for each week from the current week to the first week that was booked in the given budget
     */
    public List<AggregatedRecord> getWeeklyAggregationForBudgetWithTax(long budgetId) {
        //ToDo
        List<WeeklyAggregatedRecordWithTitleAndTaxBean> workRecords = workRecordRepository.aggregateByWeekAndPersonForBudgetWithTax(budgetId);
        List<WeeklyAggregatedRecordWithTaxBean> planRecords = planRecordRepository.aggregateByWeekForBudgetWithTax(budgetId);

        List<WeeklyAggregatedRecordWithTitleAndTaxBean> manualWorkRecords = manualRecordRepository.aggregateByWeekForBudgetWithTax(budgetId);
        workRecords.addAll(manualWorkRecords);

        MonthlyStats monthlyStats = new MonthlyStats(budgetId, workRecordRepository, planRecordRepository);

        return recordJoiner.joinWeeklyByMonthFraction(workRecords, planRecords, monthlyStats);
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
        //ToDo
        List<MonthlyAggregatedRecordWithTaxBean> planRecords = planRecordRepository.aggregateByMonthAndBudgetWithTax(budgetId);
        List<MonthlyAggregatedRecordWithTaxBean> workRecords = workRecordRepository.aggregateByMonthAndBudgetWithTax(budgetId);
        List<MonthlyAggregatedRecordWithTaxBean> manualWorkRecords = manualRecordRepository.aggregateByMonthAndBudgetWithTax(budgetId);
        workRecords.addAll(manualWorkRecords);
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
    public List<AggregatedRecord> getWeeklyAggregationForBudgetsWithTaxes(BudgetTagFilter budgetFilter) {
        List<WeeklyAggregatedRecordWithTaxBean> planRecords;
        List<WeeklyAggregatedRecordWithTitleAndTaxBean> workRecords;
        List<WeeklyAggregatedRecordWithTitleAndTaxBean> manualWorkRecords;

        //ToDo
        if (budgetFilter.getSelectedTags().isEmpty()) {
            workRecords = workRecordRepository.aggregateByWeekAndPersonForBudgetsWithTax(budgetFilter.getProjectId());
            planRecords = planRecordRepository.aggregateByWeekForBudgetsWithTax(budgetFilter.getProjectId());
            manualWorkRecords = manualRecordRepository.aggregateByWeekForBudgetsWithTax(budgetFilter.getProjectId());
        } else {
            workRecords = workRecordRepository.aggregateByWeekAndPersonForBudgetsWithTax(budgetFilter.getProjectId(), budgetFilter.getSelectedTags());
            planRecords = planRecordRepository.aggregateByWeekForBudgetsWithTax(budgetFilter.getProjectId(), budgetFilter.getSelectedTags());
            manualWorkRecords = manualRecordRepository.aggregateByWeekForBudgetsWithTax(budgetFilter.getProjectId(), budgetFilter.getSelectedTags());
        }
        workRecords.addAll(manualWorkRecords);
        MonthlyStats monthlyStats = new MonthlyStats(budgetFilter, workRecordRepository, planRecordRepository);
        return recordJoiner.joinWeeklyByMonthFraction(workRecords, planRecords, monthlyStats);
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
        //ToDo
        List<MonthlyAggregatedRecordWithTaxBean> planRecords;
        List<MonthlyAggregatedRecordWithTaxBean> workRecords;
        List<MonthlyAggregatedRecordWithTaxBean> manualWorkRecords;

        if (budgetFilter.getSelectedTags().isEmpty()) {
            workRecords = workRecordRepository.aggregateByMonthWithTax(budgetFilter.getProjectId());
            planRecords = planRecordRepository.aggregateByMonthWithTax(budgetFilter.getProjectId());
            manualWorkRecords = manualRecordRepository.aggregateByMonthWithTax(budgetFilter.getProjectId());
        } else {
            workRecords = workRecordRepository.aggregateByMonthAndBudgetTagsWithTax(budgetFilter.getProjectId(), budgetFilter.getSelectedTags());
            planRecords = planRecordRepository.aggregateByMonthAndBudgetTagsWithTax(budgetFilter.getProjectId(), budgetFilter.getSelectedTags());
            manualWorkRecords = manualRecordRepository.aggregateByMonthAndBudgetTagsWithTax(budgetFilter.getProjectId(), budgetFilter.getSelectedTags());
        }

        workRecords.addAll(manualWorkRecords);

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
        List<WorkRecord> workRecords = recordMapper.map(ListUtil.toArrayList(workRecordRepository.findAll(query)));
        switch (filter.getColumnToSort().getObject()) {
            case BUDGET:
                workRecords.sort(Comparator.comparing(WorkRecord::getBudgetName));
                break;
            case NAME:
                workRecords.sort(Comparator.comparing(WorkRecord::getPersonName));
                break;
            case DAILY_RATE:
                workRecords.sort(Comparator.comparing(WorkRecord::getDailyRate));
                break;
            case DATE:
                workRecords.sort(Comparator.comparing(WorkRecord::getDate));
                break;
            case HOURS:
                workRecords.sort(Comparator.comparing(WorkRecord::getHours));
                break;
            case BUDGET_BURNED:
                workRecords.sort(Comparator.comparing(WorkRecord::getBudgetBurned));
                break;
        }
        if (filter.getSortType().getObject().equals("Descending")) {
            Collections.reverse(workRecords);
        }
        return workRecords;
    }

    public void saveDailyRateForWorkRecord(WorkRecord record) {
        WorkRecordEntity entity = workRecordRepository.findOne(record.getId());
        entity.setDailyRate(record.getDailyRate());
        entity.setEditedManually(record.isEditedManually());
        workRecordRepository.save(entity);
    }

    /**
     *
     * @param projectId The project ID.
     * @return All missing daily rates for the project.
     */
    public List<MissingDailyRateBean> getMissingDailyRatesForProject(long projectId) {
        List<MissingDailyRateBean> result = new ArrayList<>();
        List<WorkRecordEntity> dailyRatesForProject = workRecordRepository.findByProjectId(projectId);

        //If we only have one daily Rate, see if it's zero and return as missing.
        if(dailyRatesForProject.size() == 1){
            WorkRecordEntity wr = dailyRatesForProject.get(0);
            if(wr.getDailyRate().isZero()){
                result.add(new MissingDailyRateBean(wr.getPerson().getId(), wr.getPerson().getName(), wr.getDate(), wr.getDate()));
                return result;
            }
        }

        //Sort the daily rates by person names, record dates and record budget.
        //This is needed in the loop below.
        dailyRatesForProject.sort((o1, o2) -> {
            int names = o1.getPerson().getName().compareTo(o2.getPerson().getName());
            if (names == 0) {
                int dates = o1.getBudget().getName().compareTo(o2.getBudget().getName());
                if(dates == 0){
                    return o1.getDate().compareTo(o2.getDate());
                }else{
                    return dates;
                }
            } else {
                return names;
            }
        });

        Date endDate = null;
        Date startDate = null;

        //We check the rates in a loop and see if any two are adjacent (are both zero)
        //and then we merge those into one MissingDailyRateBean
        for(int i = 0; i < dailyRatesForProject.size() - 1; i++){
            WorkRecordEntity rate1 = dailyRatesForProject.get(i);
            WorkRecordEntity rate2 = dailyRatesForProject.get(i+1);
            if(!rate1.getDailyRate().isZero()){
                continue;
            }
            if(startDate == null) {
                endDate = rate2.getDate();
                startDate = rate1.getDate();
            }

            if(rate1.getDailyRate().isZero() && rate2.getDailyRate().isZero()
                    && rate1.getPerson().getId() == rate2.getPerson().getId()
                    && rate1.getBudget().getId() == rate2.getBudget().getId()){
                endDate = rate2.getDate();

                //If we are the end of the list.
                if(i+1 == dailyRatesForProject.size() - 1){
                    result.add(new MissingDailyRateBean(rate1.getPerson().getId(),
                            rate1.getPerson().getName(), startDate, endDate));
                }
            }else{
                //If the next rate belongs to a different person and is not zero, then set the end date of the missing record to the last one checked for this person.
                if(rate1.getPerson().getId() != rate2.getPerson().getId() || !rate2.getDailyRate().isZero()){
                    endDate = rate1.getDate();
                }
                MissingDailyRateBean missingDailyRateBean = new MissingDailyRateBean(rate1.getPerson().getId(),
                        rate1.getPerson().getName(), startDate, endDate);
                result.add(missingDailyRateBean);
                startDate = null;
            }
        }

        return result;
    }

    /**
     *
     * @param personId The Id of the person.
     * @return All daily rates for that person which are zero.
     */
    public List<MissingDailyRateForBudgetBean> getMissingDailyRatesForPerson(long personId) {
        List<MissingDailyRateForBudgetBean> result = new ArrayList<>();
        List<WorkRecordEntity> dailyRatesForPerson = workRecordRepository.findByPersonId(personId);

        //If there is only one entity
        if(dailyRatesForPerson.size() == 1){
            WorkRecordEntity wr = dailyRatesForPerson.get(0);
            if(wr.getDailyRate().isZero()){
                result.add(new MissingDailyRateForBudgetBean(wr.getPerson().getId(), wr.getPerson().getName(), wr.getDate(), wr.getDate(), wr.getBudget().getName()));
                return result;
            }
        }

        //Sort by date and budget name, this is needed in the loop below.
        dailyRatesForPerson.sort((o1, o2) -> {
            int dates = o1.getBudget().getName().compareTo(o2.getBudget().getName());
            if(dates == 0){
                return o1.getDate().compareTo(o2.getDate());
            }else{
                return dates;
            }
        });

        Date startDate = null;

        //We check the rates in a loop and see if any two are adjacent (are both zero)
        //and then we merge those into one MissingDailyRateForBudgetBean
        for(int i = 0; i < dailyRatesForPerson.size() - 1; i++){
            WorkRecordEntity rate1 = dailyRatesForPerson.get(i);
            WorkRecordEntity rate2 = dailyRatesForPerson.get(i+1);
            if(!rate1.getDailyRate().isZero()){
                if(i+1 == dailyRatesForPerson.size()-1 && rate2.getDailyRate().isZero()) {
                    result.add(new MissingDailyRateForBudgetBean(rate2.getPerson().getId(),
                            rate2.getPerson().getName(), rate2.getDate(), rate2.getDate(), rate2.getBudget().getName()));
                    return result;
                }else{
                    continue;
                }
            }
            if(startDate == null) {
                startDate = rate1.getDate();
            }

            if(rate1.getDailyRate().isZero() && rate2.getDailyRate().isZero()
                    && rate1.getBudget().getId() == rate2.getBudget().getId()){
                //If we are the end of the list
                if(i+1 == dailyRatesForPerson.size() - 1){
                    result.add(new MissingDailyRateForBudgetBean(rate1.getPerson().getId(),
                            rate1.getPerson().getName(), startDate, rate2.getDate(), rate1.getBudget().getName()));
                }
            }else{
                MissingDailyRateForBudgetBean missingDailyRateBean = new MissingDailyRateForBudgetBean(rate1.getPerson().getId(),
                        rate1.getPerson().getName(), startDate, rate1.getDate(), rate1.getBudget().getName());
                result.add(missingDailyRateBean);
                startDate = null;
            }
        }
        return result;
    }
}
