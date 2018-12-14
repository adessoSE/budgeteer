package de.adesso.budgeteer.persistence.record;

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
    @Query("delete from PlanRecordEntity r where r.id in (select p.id from PlanRecordEntity p where p.importRecord.project.id = :projectId )")
    void deleteByImportAndProjectId(@Param("projectId") long projectId);

    @Override
    @Modifying
    @Query("delete from PlanRecordEntity r where r.budget.id in ( select b.id from BudgetEntity b where b.project.id = :projectId)")
    void deleteByProjectId(@Param("projectId") long projectId);

    @Override
    @Modifying
    @Query("update PlanRecordEntity r set r.dailyRate = :dailyRate where r.budget.id=:budgetId and r.person.id=:personId and r.date between :fromDate and :toDate")
    void updateDailyRates(@Param("budgetId") long budgetId, @Param("personId") long personId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("dailyRate") Money dailyRate);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.person.id=:personId group by r.year, r.week order by r.year, r.week")
    List<WeeklyAggregatedRecordBean> aggregateByWeekAndPerson(@Param("personId") long personId);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.person.id=:personId group by r.year, r.month order by r.year, r.month")
    List<MonthlyAggregatedRecordBean> aggregateByMonthAndPerson(@Param("personId") long personId);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.budget.id=:budgetId group by r.year, r.week order by r.year, r.week")
    List<WeeklyAggregatedRecordBean> aggregateByWeekAndBudget(@Param("budgetId") long budgetId);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordWithTaxBean(r.year, r.month, r.week, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate ) from PlanRecordEntity r where r.budget.id=:budgetId group by r.year, r.month, r.week, r.dailyRate, r.budget.contract.taxRate order by r.year, r.month, r.week")
    List<WeeklyAggregatedRecordWithTaxBean> aggregateByWeekAndBudgetWithTax(@Param("budgetId") long budgetId);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.budget.id=:budgetId group by r.year, r.month order by r.year, r.month")
    List<MonthlyAggregatedRecordBean> aggregateByMonthAndBudget(@Param("budgetId") long budgetId);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean(r.year, r.month, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate) from PlanRecordEntity r where r.budget.id=:budgetId group by r.year, r.month, r.dailyRate, r.budget.contract.taxRate order by r.year, r.month")
    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthAndBudgetWithTax(@Param("budgetId") long budgetId);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) group by r.year, r.week order by r.year, r.week")
    List<WeeklyAggregatedRecordBean> aggregateByWeekAndBudgetTags(@Param("projectId") long projectId, @Param("tags") List<String> tags);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordWithTaxBean(r.year, r.month, r.week, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate ) from PlanRecordEntity r join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) group by r.year, r.month, r.week, r.dailyRate, r.budget.contract.taxRate order by r.year, r.month, r.week")
    List<WeeklyAggregatedRecordWithTaxBean> aggregateByWeekAndBudgetTagsWithTax(@Param("projectId") long projectId, @Param("tags") List<String> tags);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) group by r.year, r.month order by r.year, r.month")
    List<MonthlyAggregatedRecordBean> aggregateByMonthAndBudgetTags(@Param("projectId") long projectId, @Param("tags") List<String> tags);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean(r.year, r.month, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate) from PlanRecordEntity r join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) group by r.year, r.month, r.dailyRate, r.budget.contract.taxRate order by r.year, r.month")
    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthAndBudgetTagsWithTax(@Param("projectId") long projectId, @Param("tags") List<String> tags);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.budget.project.id=:projectId and r.date >= :startDate group by r.year, r.week order by r.year, r.week")
    List<WeeklyAggregatedRecordBean> aggregateByWeekForProject(@Param("projectId") long projectId, @Param("startDate") Date start);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.person.id=:personId and r.date >= :startDate group by r.year, r.week order by r.year, r.week")
    List<WeeklyAggregatedRecordBean> aggregateByWeekForPerson(@Param("personId") long personId, @Param("startDate") Date start);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.budget.id=:budgetId and r.date >= :startDate group by r.year, r.week order by r.year, r.week")
    List<WeeklyAggregatedRecordBean> aggregateByWeekForBudget(@Param("budgetId") long budgetId, @Param("startDate") Date start);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordWithTaxBean(r.year, r.month, r.week, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate ) from PlanRecordEntity r where r.budget.id=:budgetId and r.date >= :startDate group by r.year, r.month, r.week, r.dailyRate, r.budget.contract.taxRate order by r.year, r.week")
    List<WeeklyAggregatedRecordWithTaxBean> aggregateByWeekForBudgetWithTax(@Param("budgetId") long budgetId, @Param("startDate") Date start);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordWithTaxBean(r.year, r.month, r.week, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate ) from PlanRecordEntity r where r.budget.id=:budgetId group by r.year, r.month, r.week, r.dailyRate, r.budget.contract.taxRate order by r.year, r.week")
    List<WeeklyAggregatedRecordWithTaxBean> aggregateByWeekForBudgetWithTax(@Param("budgetId") long budgetId);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8, b.name ) from PlanRecordEntity r join r.budget b where r.person.id=:personId and r.date >= :startDate group by r.year, r.week, b.name order by b.name, r.year, r.week")
    List<WeeklyAggregatedRecordWithTitleBean> aggregateByWeekAndBudgetForPerson(@Param("personId") long personId, @Param("startDate") Date start);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8, p.name ) from PlanRecordEntity r join r.person p where r.budget.id=:budgetId and r.date >= :startDate group by r.year, r.week, p.name order by p.name, r.year, r.week")
    List<WeeklyAggregatedRecordWithTitleBean> aggregateByWeekAndPersonForBudget(@Param("budgetId") long budgetId, @Param("startDate") Date startDate);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, r.week, sum(r.minutes),r.dailyRate, r.budget.contract.taxRate, p.name ) from PlanRecordEntity r join r.person p where r.budget.id=:budgetId and r.date >= :startDate group by r.year, r.month, r.week,r.dailyRate, r.budget.contract.taxRate, p.name order by p.name, r.year, r.week")
    List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekAndPersonForBudgetWithTax(@Param("budgetId") long budgetId, @Param("startDate") Date startDate);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8, p.name ) from PlanRecordEntity r join r.person p join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) and r.date >= :startDate group by r.year, r.week, p.name order by p.name, r.year, r.week")
    List<WeeklyAggregatedRecordWithTitleBean> aggregateByWeekAndPersonForBudgets(@Param("projectId") long projectId, @Param("tags") List<String> tags, @Param("startDate") Date startDate);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8, p.name ) from PlanRecordEntity r join r.person p join r.budget b where b.project.id=:projectId and r.date >= :startDate group by r.year, r.week, p.name order by p.name, r.year, r.week")
    List<WeeklyAggregatedRecordWithTitleBean> aggregateByWeekAndPersonForBudgets(@Param("projectId") long projectId, @Param("startDate") Date startDate);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, r.week, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate, p.name ) from PlanRecordEntity r join r.person p join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) and r.date >= :startDate group by r.year, r.month, r.week, r.dailyRate, r.budget.contract.taxRate, p.name order by p.name, r.year, r.week")
    List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekAndPersonForBudgetsWithTax(@Param("projectId") long projectId, @Param("tags") List<String> tags, @Param("startDate") Date startDate);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, r.week, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate, p.name ) from PlanRecordEntity r join r.person p join r.budget b where b.project.id=:projectId and r.date >= :startDate group by r.year, r.month, r.week, r.dailyRate, r.budget.contract.taxRate, p.name order by p.name, r.year, r.week")
    List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekAndPersonForBudgetsWithTax(@Param("projectId") long projectId, @Param("startDate") Date startDate);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) and r.date >= :startDate group by r.year, r.week order by r.year, r.week")
    List<WeeklyAggregatedRecordBean> aggregateByWeekForBudgets(@Param("projectId") long projectId, @Param("tags") List<String> tags, @Param("startDate") Date start);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r join r.budget b where b.project.id=:projectId and r.date >= :startDate group by r.year, r.week order by r.year, r.week")
    List<WeeklyAggregatedRecordBean> aggregateByWeekForBudgets(@Param("projectId") long projectId, @Param("startDate") Date start);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordWithTaxBean(r.year, r.month, r.week, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate ) from PlanRecordEntity r join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) and r.date >= :startDate group by r.year, r.month, r.week, r.dailyRate, r.budget.contract.taxRate order by r.year, r.month, r.week")
    List<WeeklyAggregatedRecordWithTaxBean> aggregateByWeekForBudgetsWithTax(@Param("projectId") long projectId, @Param("tags") List<String> tags, @Param("startDate") Date start);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordWithTaxBean(r.year, r.month, r.week, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate ) from PlanRecordEntity r join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) group by r.year, r.month, r.week, r.dailyRate, r.budget.contract.taxRate order by r.year, r.month, r.week")
    List<WeeklyAggregatedRecordWithTaxBean> aggregateByWeekForBudgetsWithTax(@Param("projectId") long projectId, @Param("tags") List<String> tags);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordWithTaxBean(r.year, r.month, r.week, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate ) from PlanRecordEntity r join r.budget b where b.project.id=:projectId and r.date >= :startDate group by r.year, r.month, r.week, r.dailyRate, r.budget.contract.taxRate order by r.year, r.month, r.week")
    List<WeeklyAggregatedRecordWithTaxBean> aggregateByWeekForBudgetsWithTax(@Param("projectId") long projectId, @Param("startDate") Date start);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordWithTaxBean(r.year, r.month, r.week, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate ) from PlanRecordEntity r join r.budget b where b.project.id=:projectId group by r.year, r.month, r.week, r.dailyRate, r.budget.contract.taxRate order by r.year, r.month, r.week")
    List<WeeklyAggregatedRecordWithTaxBean> aggregateByWeekForBudgetsWithTax(@Param("projectId") long projectId);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordBean(r.year, r.week, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r join r.budget b where b.project.id=:projectId group by r.year, r.week order by r.year, r.week")
    List<WeeklyAggregatedRecordBean> aggregateByWeek(@Param("projectId") long projectId);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordWithTaxBean(r.year, r.month, r.week, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate) from PlanRecordEntity r join r.budget b where b.project.id=:projectId group by r.year, r.month, r.week, r.dailyRate, r.budget.contract.taxRate order by r.year, r.week")
    List<WeeklyAggregatedRecordWithTaxBean> aggregateByWeekWithTax(@Param("projectId") long projectId);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.person.id=:personId and r.date >= :startDate group by r.year, r.month order by r.year, r.month")
    List<MonthlyAggregatedRecordBean> aggregateByMonthForPerson(@Param("personId") long personId, @Param("startDate") Date startDate);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordWithTitleBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8, b.name ) from PlanRecordEntity r join r.budget b where r.person.id=:personId and r.date >= :startDate group by r.year, r.month, b.name order by b.name, r.year, r.month")
    List<MonthlyAggregatedRecordWithTitleBean> aggregateByMonthAndBudgetForPerson(@Param("personId") long personId, @Param("startDate") Date startDate);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r where r.budget.id=:budgetId and r.date >= :startDate group by r.year, r.month order by r.year, r.month")
    List<MonthlyAggregatedRecordBean> aggregateByMonthForBudget(@Param("budgetId") long budgetId, @Param("startDate") Date startDate);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean(r.year, r.month, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate ) from PlanRecordEntity r where r.budget.id=:budgetId and r.date >= :startDate group by r.year, r.month, r.dailyRate, r.budget.contract.taxRate order by r.year, r.month")
    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthForBudgetWithTax(@Param("budgetId") long budgetId, @Param("startDate") Date startDate);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean(r.year, r.month, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate ) from PlanRecordEntity r where r.budget.id=:budgetId group by r.year, r.month, r.dailyRate, r.budget.contract.taxRate order by r.year, r.month")
    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthForBudgetWithTax(@Param("budgetId") long budgetId);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordWithTitleBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8, p.name ) from PlanRecordEntity r join r.person p where r.budget.id=:budgetId and r.date >= :startDate group by r.year, r.month, p.name order by p.name, r.year, r.month")
    List<MonthlyAggregatedRecordWithTitleBean> aggregateByMonthAndPersonForBudget(@Param("budgetId") long budgetId, @Param("startDate") Date startDate);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8, p.name, r.budget.contract.taxRate ) from PlanRecordEntity r join r.person p where r.budget.id=:budgetId and r.date >= :startDate group by r.year, r.month, p.name, r.budget.contract.taxRate order by p.name, r.year, r.month")
    List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateByMonthAndPersonForBudgetWithTax(@Param("budgetId") long budgetId, @Param("startDate") Date startDate);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) and r.date >= :startDate group by r.year, r.month order by r.year, r.month")
    List<MonthlyAggregatedRecordBean> aggregateByMonthForBudgets(@Param("projectId") long projectId, @Param("tags") List<String> tags, @Param("startDate") Date startDate);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r join r.budget b where b.project.id=:projectId and r.date >= :startDate group by r.year, r.month order by r.year, r.month")
    List<MonthlyAggregatedRecordBean> aggregateByMonthForBudgets(@Param("projectId") long projectId, @Param("startDate") Date startDate);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean(r.year, r.month, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate ) from PlanRecordEntity r join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) and r.date >= :startDate group by r.year, r.month, r.dailyRate, r.budget.contract.taxRate order by r.year, r.month")
    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthForBudgetsWithTax(@Param("projectId") long projectId, @Param("tags") List<String> tags, @Param("startDate") Date startDate);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean(r.year, r.month, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate ) from PlanRecordEntity r join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) group by r.year, r.month, r.dailyRate, r.budget.contract.taxRate order by r.year, r.month")
    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthForBudgetsWithTax(@Param("projectId") long projectId, @Param("tags") List<String> tags);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean(r.year, r.month, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate ) from PlanRecordEntity r join r.budget b where b.project.id=:projectId and r.date >= :startDate group by r.year, r.month, r.dailyRate, r.budget.contract.taxRate order by r.year, r.month")
    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthForBudgetsWithTax(@Param("projectId") long projectId, @Param("startDate") Date startDate);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean(r.year, r.month, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate ) from PlanRecordEntity r join r.budget b where b.project.id=:projectId group by r.year, r.month, r.dailyRate, r.budget.contract.taxRate order by r.year, r.month")
    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthForBudgetsWithTax(@Param("projectId") long projectId);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8 ) from PlanRecordEntity r join r.budget b where b.project.id=:projectId group by r.year, r.month order by r.year, r.month")
    List<MonthlyAggregatedRecordBean> aggregateByMonth(@Param("projectId") long projectId);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean(r.year, r.month, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate) from PlanRecordEntity r join r.budget b where b.project.id=:projectId group by r.year, r.month, r.dailyRate, r.budget.contract.taxRate order by r.year, r.month")
    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthWithTax(@Param("projectId") long projectId);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordWithTitleBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8, p.name ) from PlanRecordEntity r join r.person p join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) and r.date >= :startDate group by r.year, r.month, p.name order by p.name, r.year, r.month")
    List<MonthlyAggregatedRecordWithTitleBean> aggregateByMonthAndPersonForBudgets(@Param("projectId") long projectId, @Param("tags") List<String> tags, @Param("startDate") Date startDate);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordWithTitleBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8, p.name ) from PlanRecordEntity r join r.person p join r.budget b where b.project.id=:projectId and r.date >= :startDate group by r.year, r.month, p.name order by p.name, r.year, r.month")
    List<MonthlyAggregatedRecordWithTitleBean> aggregateByMonthAndPersonForBudgets(@Param("projectId") long projectId, @Param("startDate") Date startDate);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8, p.name, r.budget.contract.taxRate ) from PlanRecordEntity r join r.person p join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) and r.date >= :startDate group by r.year, r.month, p.name, r.budget.contract.taxRate order by p.name, r.year, r.month")
    List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateByMonthAndPersonForBudgetsWithTax(@Param("projectId") long projectId, @Param("tags") List<String> tags, @Param("startDate") Date startDate);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8, p.name, r.budget.contract.taxRate ) from PlanRecordEntity r join r.person p join r.budget b where b.project.id=:projectId and r.date >= :startDate group by r.year, r.month, p.name, r.budget.contract.taxRate order by p.name, r.year, r.month")
    List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateByMonthAndPersonForBudgetsWithTax(@Param("projectId") long projectId, @Param("startDate") Date startDate);

    @Override
    @Query("select count (pre.id) from PlanRecordEntity pre where pre.budget.project.id = :projectId")
    Long countByProjectId(@Param("projectId") long projectId);

    @Query("select pre from PlanRecordEntity pre where pre.person.id = :personId AND pre.budget.id = :budgetId AND pre.date = :date")
    List<PlanRecordEntity> findByPersonBudgetDate(@Param("personId") long personId, @Param("budgetId") long budgetId, @Param("date") Date date);

    @Modifying
    @Query("delete from PlanRecordEntity r where r.budget.id in ( select b.id from BudgetEntity b where  b.project.id = :projectId AND b.importKey = :importKey) AND r.date >= :date")
    void deleteByBudgetKeyAndDate(@Param("projectId") long projectId,@Param("importKey") String importKey, @Param("date") Date date);

    @Override
    @Query("select pr from PlanRecordEntity pr where pr.budget.project.id = :projectId")
    List<PlanRecordEntity> findByProjectId(@Param("projectId") long projectId);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, r.week, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate, p.name ) from PlanRecordEntity r join r.person p join r.budget b where b.project.id=:projectId group by r.year, r.month, r.week, r.dailyRate, r.budget.contract.taxRate, p.name order by p.name, r.year, r.week")
    List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekAndPersonForBudgetsWithTax(@Param("projectId") long projectId);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, r.week, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate, p.name ) from PlanRecordEntity r join r.person p join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) group by r.year, r.month, r.week, r.dailyRate, r.budget.contract.taxRate, p.name order by p.name, r.year, r.week")
    List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekAndPersonForBudgetsWithTax(@Param("projectId") long projectId, @Param("tags") List<String> tags);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, r.week, sum(r.minutes), r.dailyRate, r.budget.contract.taxRate, p.name ) from PlanRecordEntity r join r.person p where r.budget.id=:budgetId group by r.year, r.month, r.week, r.dailyRate, r.budget.contract.taxRate, p.name order by p.name, r.year, r.week")
    List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekAndPersonForBudgetWithTax(@Param("budgetId") long budgetId);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, sum(r.minutes), r.dailyRate, p.name, r.budget.contract.taxRate ) from PlanRecordEntity r join r.person p where r.budget.id=:budgetId group by r.year, r.month, r.dailyRate, p.name, r.budget.contract.taxRate order by p.name, r.year, r.month")
    List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateByMonthAndPersonForBudgetWithTax(@Param("budgetId") long budgetId);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, sum(r.minutes), r.dailyRate, p.name, r.budget.contract.taxRate ) from PlanRecordEntity r join r.person p join r.budget b where b.project.id=:projectId group by r.year, r.month, p.name, r.dailyRate, r.budget.contract.taxRate order by p.name, r.year, r.month")
    List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateByMonthAndPersonForBudgetsWithTax(@Param("projectId") long projectId);

    @Override
    @Query("select new de.adesso.budgeteer.persistence.record.MonthlyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, sum(r.minutes), r.dailyRate, p.name, r.budget.contract.taxRate ) from PlanRecordEntity r join r.person p join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) group by r.year, r.month, p.name, r.dailyRate, r.budget.contract.taxRate order by p.name, r.year, r.month")
    List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateByMonthAndPersonForBudgetsWithTax(@Param("projectId") long projectId, @Param("tags") List<String> tags);
}
