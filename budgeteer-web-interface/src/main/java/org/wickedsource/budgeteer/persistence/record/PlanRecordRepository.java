package org.wickedsource.budgeteer.persistence.record;

import org.joda.money.Money;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PlanRecordRepository extends CrudRepository<PlanRecordEntity, Long> {

    /**
     * Aggregates the monetary value of all planned records in the given budget.
     *
     * @param budgetId ID of the budget whose planned spending to aggregate.
     * @return aggregated monetary value of the planned budget in cents.
     */
    @Query("select sum(record.minutes * record.dailyRate) / 60 / 8 from PlanRecordEntity record where record.budget.id = :budgetId")
    public double getPlannedBudget(@Param("budgetId") long budgetId);

    @Modifying
    @Query("update PlanRecordEntity r set r.dailyRate = :dailyRate where r.budget.id=:budgetId and r.person.id=:personId and r.date between :fromDate and :toDate")
    public void updateDailyRates(@Param("budgetId") long budgetId, @Param("personId") long personId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("dailyRate") Money dailyRate);

    @Query("select new org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.person.id=:personId group by r.year, r.week order by r.year, r.week")
    public List<WeeklyAggregatedRecordBean> aggregateByWeekAndPerson(@Param("personId") long personId);

    @Query("select new org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.person.id=:personId group by r.year, r.month order by r.year, r.month")
    public List<MonthlyAggregatedRecordBean> aggregateByMonthAndPerson(@Param("personId") long personId);

    @Query("select new org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.budget.id=:budgetId group by r.year, r.week order by r.year, r.week")
    public List<WeeklyAggregatedRecordBean> aggregateByWeekAndBudget(@Param("budgetId") long budgetId);

    @Query("select new org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.budget.id=:budgetId group by r.year, r.month order by r.year, r.month")
    public List<MonthlyAggregatedRecordBean> aggregateByMonthAndBudget(@Param("budgetId") long budgetId);

    @Query("select new org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) group by r.year, r.week order by r.year, r.week")
    public List<WeeklyAggregatedRecordBean> aggregateByWeekAndBudgetTags(@Param("projectId") long projectId, @Param("tags") List<String> tags);

    @Query("select new org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) group by r.year, r.month order by r.year, r.month")
    public List<MonthlyAggregatedRecordBean> aggregateByMonthAndBudgetTags(@Param("projectId") long projectId, @Param("tags") List<String> tags);

}
