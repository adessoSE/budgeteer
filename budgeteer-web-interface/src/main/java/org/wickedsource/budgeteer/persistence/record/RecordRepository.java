package org.wickedsource.budgeteer.persistence.record;

import org.joda.money.Money;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface RecordRepository {

    void deleteByImport(long importId);

    void deleteByProjectId(long projectId);

    List<WeeklyAggregatedRecordBean> aggregateByWeekAndPerson(long personId);

    List<MonthlyAggregatedRecordBean> aggregateByMonthAndPerson(long personId);

    List<WeeklyAggregatedRecordBean> aggregateByWeekAndBudget(long budgetId);

    void updateDailyRates(long budgetId, long personId, Date fromDate, Date toDate, Money dailyRate);

    List<MonthlyAggregatedRecordBean> aggregateByMonthAndBudget(long budgetId);

    List<WeeklyAggregatedRecordBean> aggregateByWeekAndBudgetTags(long projectId, List<String> tags);

    List<MonthlyAggregatedRecordBean> aggregateByMonthAndBudgetTags(long projectId, List<String> tags);

    List<WeeklyAggregatedRecordBean> aggregateByWeekForProject(long projectId, Date start);

    List<WeeklyAggregatedRecordBean> aggregateByWeekForPerson(long personId, Date start);

    List<WeeklyAggregatedRecordWithTitleBean> aggregateByWeekAndBudgetForPerson(long personId, Date start);

    List<WeeklyAggregatedRecordWithTitleBean> aggregateByWeekAndPersonForBudget(long budgetId, Date startDate);

    List<WeeklyAggregatedRecordBean> aggregateByWeekForBudget(long budgetId, Date start);

    List<WeeklyAggregatedRecordWithTitleBean> aggregateByWeekAndPersonForBudgets(long projectId, List<String> tags, Date startDate);

    List<WeeklyAggregatedRecordBean> aggregateByWeekForBudgets(long projectId, List<String> tags, Date start);

    List<MonthlyAggregatedRecordBean> aggregateByMonthForPerson(long personId, Date startDate);

    List<MonthlyAggregatedRecordWithTitleBean> aggregateByMonthAndBudgetForPerson(long personId, Date startDate);

    List<MonthlyAggregatedRecordBean> aggregateByMonthForBudget(long budgetId, Date startDate);

    List<MonthlyAggregatedRecordWithTitleBean> aggregateByMonthAndPersonForBudget(long budgetId, Date startDate);

    List<MonthlyAggregatedRecordBean> aggregateByMonthForBudgets(long projectId, List<String> tags, Date startDate);

    List<MonthlyAggregatedRecordWithTitleBean> aggregateByMonthAndPersonForBudgets(long projectId, List<String> tags, Date startDate);

}
