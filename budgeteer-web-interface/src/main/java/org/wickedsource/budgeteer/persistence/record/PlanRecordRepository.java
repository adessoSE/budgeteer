package org.wickedsource.budgeteer.persistence.record;

import org.joda.money.Money;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PlanRecordRepository extends CrudRepository<PlanRecordEntity, Long>, RecordRepository {

    /**
     * Aggregates the monetary value of all planned records in the given budget.
     *
     * @param budgetId ID of the budget whose planned spending to aggregate.
     * @return aggregated monetary value of the planned budget in cents.
     */
    @Query("select sum(record.minutes * record.dailyRate) / 60 / 8 from PlanRecordEntity record where record.budget.id = :budgetId")
    Double getPlannedBudget(@Param("budgetId") long budgetId);

    @Override
    @Modifying
    @Query("delete from PlanRecordEntity r where r.importRecord.id = :importId")
    void deleteByImport(@Param("importId") long importId);

    @Override
    @Modifying
    @Query("update PlanRecordEntity r set r.dailyRate = :dailyRate where r.budget.id=:budgetId and r.person.id=:personId and r.date between :fromDate and :toDate")
    void updateDailyRates(@Param("budgetId") long budgetId, @Param("personId") long personId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("dailyRate") Money dailyRate);

    @Override
    @Query("select new org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.person.id=:personId group by r.year, r.week order by r.year, r.week")
    List<WeeklyAggregatedRecordBean> aggregateByWeekAndPerson(@Param("personId") long personId);

    @Override
    @Query("select new org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.person.id=:personId group by r.year, r.month order by r.year, r.month")
    List<MonthlyAggregatedRecordBean> aggregateByMonthAndPerson(@Param("personId") long personId);

    @Override
    @Query("select new org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.budget.id=:budgetId group by r.year, r.week order by r.year, r.week")
    List<WeeklyAggregatedRecordBean> aggregateByWeekAndBudget(@Param("budgetId") long budgetId);

    @Override
    @Query("select new org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.budget.id=:budgetId group by r.year, r.month order by r.year, r.month")
    List<MonthlyAggregatedRecordBean> aggregateByMonthAndBudget(@Param("budgetId") long budgetId);

    @Override
    @Query("select new org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) group by r.year, r.week order by r.year, r.week")
    List<WeeklyAggregatedRecordBean> aggregateByWeekAndBudgetTags(@Param("projectId") long projectId, @Param("tags") List<String> tags);

    @Override
    @Query("select new org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) group by r.year, r.month order by r.year, r.month")
    List<MonthlyAggregatedRecordBean> aggregateByMonthAndBudgetTags(@Param("projectId") long projectId, @Param("tags") List<String> tags);

    @Override
    @Query("select new org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.budget.project.id=:projectId and r.date >= :startDate group by r.year, r.week order by r.year, r.week")
    List<WeeklyAggregatedRecordBean> aggregateByWeekForProject(@Param("projectId") long projectId, @Param("startDate") Date start);

    @Override
    @Query("select new org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.person.id=:personId and r.date >= :startDate group by r.year, r.week order by r.year, r.week")
    List<WeeklyAggregatedRecordBean> aggregateByWeekForPerson(@Param("personId") long personId, @Param("startDate") Date start);

    @Override
    @Query("select new org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.budget.id=:budgetId and r.date >= :startDate group by r.year, r.week order by r.year, r.week")
    List<WeeklyAggregatedRecordBean> aggregateByWeekForBudget(@Param("budgetId") long budgetId, @Param("startDate") Date start);

    @Override
    @Query("select new org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8, b.name ) from PlanRecordEntity r join r.budget b where r.person.id=:personId and r.date >= :startDate group by r.year, r.week, b.name order by b.name, r.year, r.week")
    List<WeeklyAggregatedRecordWithTitleBean> aggregateByWeekAndBudgetForPerson(@Param("personId") long personId, @Param("startDate") Date start);

    @Override
    @Query("select new org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8, p.name ) from PlanRecordEntity r join r.person p where r.budget.id=:budgetId and r.date >= :startDate group by r.year, r.week, p.name order by p.name, r.year, r.week")
    List<WeeklyAggregatedRecordWithTitleBean> aggregateByWeekAndPersonForBudget(@Param("budgetId") long budgetId, @Param("startDate") Date startDate);

    @Override
    @Query("select new org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8, p.name ) from PlanRecordEntity r join r.person p join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) and r.date >= :startDate group by r.year, r.week, p.name order by p.name, r.year, r.week")
    List<WeeklyAggregatedRecordWithTitleBean> aggregateByWeekAndPersonForBudgets(@Param("projectId") long projectId, @Param("tags") List<String> tags, @Param("startDate") Date startDate);

    @Override
    @Query("select new org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) and r.date >= :startDate group by r.year, r.week order by r.year, r.week")
    List<WeeklyAggregatedRecordBean> aggregateByWeekForBudgets(@Param("projectId") long projectId, @Param("tags") List<String> tags, @Param("startDate") Date start);

    @Override
    @Query("select new org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.person.id=:personId and r.date >= :startDate group by r.year, r.month order by r.year, r.month")
    List<MonthlyAggregatedRecordBean> aggregateByMonthForPerson(@Param("personId") long personId, @Param("startDate") Date startDate);

    @Override
    @Query("select new org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTitleBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8, b.name ) from PlanRecordEntity r join r.budget b where r.person.id=:personId and r.date >= :startDate group by r.year, r.month, b.name order by b.name, r.year, r.month")
    List<MonthlyAggregatedRecordWithTitleBean> aggregateByMonthAndBudgetForPerson(@Param("personId") long personId, @Param("startDate") Date startDate);

    @Override
    @Query("select new org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.budget.id=:budgetId and r.date >= :startDate group by r.year, r.month order by r.year, r.month")
    List<MonthlyAggregatedRecordBean> aggregateByMonthForBudget(@Param("budgetId") long budgetId, @Param("startDate") Date startDate);

    @Override
    @Query("select new org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTitleBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8, p.name ) from PlanRecordEntity r join r.person p where r.budget.id=:budgetId and r.date >= :startDate group by r.year, r.month, p.name order by p.name, r.year, r.month")
    List<MonthlyAggregatedRecordWithTitleBean> aggregateByMonthAndPersonForBudget(@Param("budgetId") long budgetId, @Param("startDate") Date startDate);

    @Override
    @Query("select new org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) and r.date >= :startDate group by r.year, r.month order by r.year, r.month")
    List<MonthlyAggregatedRecordBean> aggregateByMonthForBudgets(@Param("projectId") long projectId, @Param("tags") List<String> tags, @Param("startDate") Date startDate);

    @Override
    @Query("select new org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTitleBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8, p.name ) from PlanRecordEntity r join r.person p join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) and r.date >= :startDate group by r.year, r.month, p.name order by p.name, r.year, r.month")
    List<MonthlyAggregatedRecordWithTitleBean> aggregateByMonthAndPersonForBudgets(@Param("projectId") long projectId, @Param("tags") List<String> tags, @Param("startDate") Date startDate);
}
