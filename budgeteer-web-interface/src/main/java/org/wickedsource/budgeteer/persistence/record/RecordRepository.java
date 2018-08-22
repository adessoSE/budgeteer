package org.wickedsource.budgeteer.persistence.record;

import org.joda.money.Money;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface RecordRepository {

    void deleteByImport(long importId);

    void deleteByImportAndProjectId(long projectId);

    void deleteByProjectId(long projectId);

    List<? extends RecordEntity> findByProjectId(long projectId);

    List<WeeklyAggregatedRecordBean> aggregateByWeekAndPerson(long personId);

    List<MonthlyAggregatedRecordBean> aggregateByMonthAndPerson(long personId);

    List<WeeklyAggregatedRecordBean> aggregateByWeekAndBudget(long budgetId);

    List<WeeklyAggregatedRecordWithTaxBean> aggregateByWeekAndBudgetWithTax(long budgetId);

    void updateDailyRates(long budgetId, long personId, Date fromDate, Date toDate, Money dailyRate);

    List<MonthlyAggregatedRecordBean> aggregateByMonthAndBudget(long budgetId);

    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthAndBudgetWithTax(long budgetId);

    List<WeeklyAggregatedRecordBean> aggregateByWeekAndBudgetTags(long projectId, List<String> tags);

    List<WeeklyAggregatedRecordWithTaxBean> aggregateByWeekAndBudgetTagsWithTax(long projectId, List<String> tags);

    List<MonthlyAggregatedRecordBean> aggregateByMonthAndBudgetTags(long projectId, List<String> tags);

    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthAndBudgetTagsWithTax(long projectId, List<String> tags);

    List<WeeklyAggregatedRecordBean> aggregateByWeekForProject(long projectId, Date start);

    List<WeeklyAggregatedRecordBean> aggregateByWeekForPerson(long personId, Date start);

    List<WeeklyAggregatedRecordWithTitleBean> aggregateByWeekAndBudgetForPerson(long personId, Date start);

    List<WeeklyAggregatedRecordWithTitleBean> aggregateByWeekAndPersonForBudget(long budgetId, Date startDate);

    List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekAndPersonForBudgetWithTax(long budgetId, Date startDate);

    List<WeeklyAggregatedRecordBean> aggregateByWeekForBudget(long budgetId, Date start);

    List<WeeklyAggregatedRecordWithTaxBean> aggregateByWeekForBudgetWithTax(long budgetId, Date start);

    List<WeeklyAggregatedRecordWithTitleBean> aggregateByWeekAndPersonForBudgets(long projectId, List<String> tags, Date startDate);

    List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekAndPersonForBudgetsWithTax(long projectId, List<String> tags, Date startDate);

    List<WeeklyAggregatedRecordWithTitleBean> aggregateByWeekAndPersonForBudgets(long projectId, Date startDate);

    List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekAndPersonForBudgetsWithTax(long projectId, Date startDate);

    List<WeeklyAggregatedRecordBean> aggregateByWeekForBudgets(long projectId, List<String> tags, Date start);

    List<WeeklyAggregatedRecordWithTaxBean> aggregateByWeekForBudgetsWithTax(long projectId, List<String> tags, Date start);

    List<MonthlyAggregatedRecordBean> aggregateByMonthForPerson(long personId, Date startDate);

    List<MonthlyAggregatedRecordWithTitleBean> aggregateByMonthAndBudgetForPerson(long personId, Date startDate);

    List<MonthlyAggregatedRecordBean> aggregateByMonthForBudget(long budgetId, Date startDate);

    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthForBudgetWithTax(long budgetId, Date startDate);

    List<MonthlyAggregatedRecordWithTitleBean> aggregateByMonthAndPersonForBudget(long budgetId, Date startDate);

    List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateByMonthAndPersonForBudgetWithTax(long budgetId, Date startDate);

    List<MonthlyAggregatedRecordBean> aggregateByMonthForBudgets(long projectId, List<String> tags, Date startDate);

    List<MonthlyAggregatedRecordBean> aggregateByMonthForBudgets(long projectId, Date startDate);

    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthForBudgetsWithTax(long projectId, List<String> tags, Date startDate);

    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthForBudgetsWithTax(long projectId, Date startDate);

    List<MonthlyAggregatedRecordBean> aggregateByMonth(long projectId);

    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthWithTax(long projectId);

    List<WeeklyAggregatedRecordBean> aggregateByWeek(long projectId);

    List<WeeklyAggregatedRecordWithTaxBean> aggregateByWeekWithTax(long projectId);

    List<MonthlyAggregatedRecordWithTitleBean> aggregateByMonthAndPersonForBudgets(long projectId, List<String> tags, Date startDate);

    List<MonthlyAggregatedRecordWithTitleBean> aggregateByMonthAndPersonForBudgets(long projectId, Date startDate);

    List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateByMonthAndPersonForBudgetsWithTax(long projectId, List<String> tags, Date startDate);

    List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateByMonthAndPersonForBudgetsWithTax(long projectId, Date startDate);

    Long countByProjectId(long projectId);

    List<WeeklyAggregatedRecordBean> aggregateByWeekForBudgets(long projectId, Date start);

    List<WeeklyAggregatedRecordWithTaxBean> aggregateByWeekForBudgetsWithTax(long projectId, Date start);

    List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekAndPersonForBudgetsWithTax(long projectId);

    List<WeeklyAggregatedRecordWithTaxBean> aggregateByWeekForBudgetsWithTax(long projectId);

    List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekAndPersonForBudgetsWithTax(long projectId, List<String> tags);

    List<WeeklyAggregatedRecordWithTaxBean> aggregateByWeekForBudgetsWithTax(long projectId, List<String> tags);

    List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekAndPersonForBudgetWithTax(long budgetId);

    List<WeeklyAggregatedRecordWithTaxBean> aggregateByWeekForBudgetWithTax(long budgetId);

    List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateByMonthAndPersonForBudgetWithTax(long budgetId);

    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthForBudgetWithTax(long budgetId);

    List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateByMonthAndPersonForBudgetsWithTax(long projectId);

    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthForBudgetsWithTax(long projectId);

    List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateByMonthAndPersonForBudgetsWithTax(long projectId, List<String> tags);

    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthForBudgetsWithTax(@Param("projectId") long projectId, @Param("tags") List<String> tags);
}
