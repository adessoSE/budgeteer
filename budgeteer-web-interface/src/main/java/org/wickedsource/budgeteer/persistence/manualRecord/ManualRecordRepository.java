package org.wickedsource.budgeteer.persistence.manualRecord;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean;
import org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTitleAndTaxBean;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean;

import java.util.Date;
import java.util.List;


public interface ManualRecordRepository extends CrudRepository<ManualRecordEntity, Long>,
        QueryDslPredicateExecutor<ManualRecordEntity>, JpaSpecificationExecutor {

    @Query("select coalesce(sum(record.cents),0) from ManualRecordEntity record where record.budget.id = :budgetId")
    Double getManualRecordSumForBudget(@Param("budgetId") long budgetId);

    @Query("select coalesce(sum(record.cents),0) from ManualRecordEntity record where record.budget.contract.id = :contractId")
    Double getManualRecordSumForContract(@Param("contractId") long contractId);

    @Query("select coalesce(sum(record.cents),0) from ManualRecordEntity record where record.budget.contract.id = :contractId and record.month = :month and record.year = :year")
    Double getManualRecordSumForContractByMonthAndYear(@Param("contractId") long contractId, @Param("month") Integer month, @Param("year") Integer year);

    @Query("select coalesce(sum(record.cents),0) from ManualRecordEntity record where record.budget.contract.id = :contractId and record.month <= :month and record.year <= :year")
    Double getManualRecordSumForContractUntilMonthAndYear(@Param("contractId") long contractId, @Param("month") Integer month, @Param("year") Integer year);

    @Query("select new org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean(r.year, r.week, coalesce(sum(r.cents),0)) from ManualRecordEntity r where r.budget.project.id=:projectId and r.date >= :startDate group by r.year, r.week order by r.year, r.week")
    List<WeeklyAggregatedRecordBean> aggregateByWeekForProject(@Param("projectId") long projectId, @Param("startDate") Date start);

    @Query("select new org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, coalesce(sum(r.cents),0), 'manual records', r.budget.contract.taxRate ) from ManualRecordEntity r join r.budget b where b.project.id=:projectId and r.date >= :startDate group by r.year, r.month, r.budget.contract.taxRate order by r.year, r.month")
    List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateByMonthForBudgetsWithTax(@Param("projectId") long projectId, @Param("startDate") Date startDate);

    @Query("select new org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, coalesce(sum(r.cents),0), 'manual records', r.budget.contract.taxRate ) from ManualRecordEntity r join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) and r.date >= :startDate group by r.year, r.month, r.budget.contract.taxRate order by r.year, r.month")
    List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateByMonthForBudgetsWithTax(@Param("projectId") long projectId, @Param("tags") List<String> tags, @Param("startDate") Date startDate);

    @Query("select new org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, coalesce(sum(r.cents),0), 'manual records', r.budget.contract.taxRate ) from ManualRecordEntity r where r.budget.id=:budgetId and r.date >= :startDate group by r.year, r.month, r.budget.contract.taxRate order by r.year, r.month")
    List<MonthlyAggregatedRecordWithTitleAndTaxBean> aggregateByMonthForBudgetWithTax(@Param("budgetId") long budgetId, @Param("startDate") Date startDate);

    @Query("select new org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean(r.year, r.month, coalesce(sum(r.cents),0), r.budget.contract.taxRate ) from ManualRecordEntity r where r.budget.id=:budgetId group by r.year, r.month, r.budget.contract.taxRate order by r.year, r.month")
    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthAndBudgetWithTax(@Param("budgetId") long budgetId);

    @Query("select new org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean(r.year, r.month, coalesce(sum(r.cents),0), r.budget.contract.taxRate) from ManualRecordEntity r join r.budget b where b.project.id=:projectId group by r.year, r.month, r.budget.contract.taxRate order by r.year, r.month")
    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthWithTax(@Param("projectId") long projectId);

    @Query("select new org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordWithTaxBean(r.year, r.month, coalesce(sum(r.cents),0), r.budget.contract.taxRate) from ManualRecordEntity r join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) group by r.year, r.month, r.budget.contract.taxRate order by r.year, r.month")
    List<MonthlyAggregatedRecordWithTaxBean> aggregateByMonthAndBudgetTagsWithTax(@Param("projectId") long projectId, @Param("tags") List<String> tags);

    @Query("select new org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, r.week, coalesce(sum(r.cents),0), r.budget.contract.taxRate, 'manual records' ) from ManualRecordEntity r join r.budget b where b.project.id=:projectId group by r.year, r.month, r.week, r.budget.contract.taxRate order by r.year, r.week")
    List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekForBudgetsWithTax(@Param("projectId") long projectId);

    @Query("select new org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, r.week, coalesce(sum(r.cents),0), r.budget.contract.taxRate, 'manual records') from ManualRecordEntity r join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) and r.date >= :startDate group by r.year, r.month, r.week, r.budget.contract.taxRate order by  r.year, r.week")
    List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekForBudgetsWithTax(@Param("projectId") long projectId, @Param("tags") List<String> tags, @Param("startDate") Date startDate);

    @Query("select new org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, r.week, coalesce(sum(r.cents),0), r.budget.contract.taxRate,'manual records' ) from ManualRecordEntity r join r.budget b where b.project.id=:projectId and r.date >= :startDate group by r.year, r.month, r.week, r.budget.contract.taxRate order by r.year, r.week")
    List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekForBudgetsWithTax(@Param("projectId") long projectId, @Param("startDate") Date startDate);

    @Query("select new org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, r.week,  coalesce(sum(r.cents),0), r.budget.contract.taxRate, 'manual records') from ManualRecordEntity r join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) group by r.year, r.month, r.week, r.budget.contract.taxRate order by r.year, r.week")
    List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekForBudgetsWithTax(@Param("projectId") long projectId, @Param("tags") List<String> tags);

    @Query("select new org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, r.week, coalesce(sum(r.cents),0), r.budget.contract.taxRate, 'manual records' ) from ManualRecordEntity r where r.budget.id=:budgetId group by r.year, r.month, r.week, r.budget.contract.taxRate order by r.year, r.week")
    List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekForBudgetWithTax(@Param("budgetId") long budgetId);

    @Query("select new org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean(r.year, r.month, r.week, coalesce(sum(r.cents),0), r.budget.contract.taxRate, 'manual records' ) from ManualRecordEntity r where r.budget.id=:budgetId and r.date >= :startDate group by r.year, r.month, r.week, r.budget.contract.taxRate order by r.year, r.week")
    List<WeeklyAggregatedRecordWithTitleAndTaxBean> aggregateByWeekForBudgetWithTax(@Param("budgetId") long budgetId, @Param("startDate") Date startDate);

    @Query("select new ManualRecordEntity(r.id, r.description, r.cents, r.budget, r.date, r.year, r.month, r.day, r.week) from ManualRecordEntity r where r.budget.id = :budgetId")
    List<ManualRecordEntity> getManualRecordByBudgetId(@Param("budgetId") long budgetId);
}