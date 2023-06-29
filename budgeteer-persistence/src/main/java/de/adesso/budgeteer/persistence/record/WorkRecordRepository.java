package de.adesso.budgeteer.persistence.record;

import de.adesso.budgeteer.persistence.budget.BudgetEntity;
import de.adesso.budgeteer.persistence.person.PersonEntity;
import org.joda.money.Money;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WorkRecordRepository
    extends CrudRepository<WorkRecordEntity, Long>,
        RecordRepository,
        JpaSpecificationExecutor<WorkRecordEntity> {

  /**
   * Aggregates the monetary value of all work records in the given budget.
   *
   * @param budgetId ID of the budget whose spending to aggregate.
   * @return aggregated monetary value of the spent budget in cents.
   */
  @Query(
      "select sum(record.minutes * record.dailyRate) / 60 / 8 from WorkRecordEntity record where record.budget.id = :budgetId")
  Double getSpentBudget(@Param("budgetId") long budgetId);

  /**
   * Calculates the monetary value of the average daily rate in the given budget.
   *
   * @param budgetId ID of the budget whose average daily rate to calculate
   * @return monetary value of the average daily rate in cents.
   */
  @Query(
      "select case when (sum(record.minutes) = 0) then 0 else (sum(record.dailyRate * record.minutes) / sum(record.minutes)) end from WorkRecordEntity record where record.budget.id=:budgetId")
  Double getAverageDailyRate(@Param("budgetId") long budgetId);

  @Query(
      "select sum(record.minutes * record.dailyRate) / 60 / 8 from WorkRecordEntity record where record.budget.id = :budgetId and record.date <= :untilDate")
  Double getSpentBudgetUntilDate(
      @Param("budgetId") long budgetId, @Param("untilDate") Date untilDate);

  @Query(
      "select sum(record.minutes * record.dailyRate) / 60 / 8 from WorkRecordEntity record where record.budget.id = :budgetId and :fromDate <= record.date and record.date <= :untilDate")
  Double getSpentBudgetInTimeRange(
      @Param("budgetId") long budgetId,
      @Param("fromDate") Date fromDate,
      @Param("untilDate") Date untilDate);

  @Query("select max(record.date) from WorkRecordEntity record where record.budget.id=:budgetId")
  Date getLatestWorkRecordDate(@Param("budgetId") long budgetId);

  @Query("select min(record.date) from WorkRecordEntity record where record.budget.id=:budgetId")
  Date getFirstWorkRecordDate(@Param("budgetId") long budgetId);

  @Query(
      "select min(record.date) from WorkRecordEntity record where record.budget.id in (:budgetIds)")
  Date getFirstWorkRecordDateByBudgetIds(@Param("budgetIds") List<Long> budgetIds);

  @Query(
      "select case when (count(*) = 0) then 0.0 else (cast(sum(record.minutes) AS double) / 60.0) end from WorkRecordEntity record where record.budget.id=:budgetId and :fromDate <= record.date and record.date <= :untilDate")
  Double getTotalHoursInTimeRange(
      @Param("budgetId") long budgetId,
      @Param("fromDate") Date fromDate,
      @Param("untilDate") Date untilDate);

  @Override
  @Modifying
  @Query("delete from WorkRecordEntity r where r.importRecord.id = :importId")
  void deleteByImport(@Param("importId") long importId);

  @Override
  @Modifying
  @Query(
      "delete from WorkRecordEntity r where r.id in (select p.id from WorkRecordEntity p where p.importRecord.project.id = :projectId )")
  void deleteByImportAndProjectId(@Param("projectId") long projectId);

  @Query(
      "select new de.adesso.budgeteer.persistence.record.MissingDailyRateBean(r.person.id, r.person.name, min(r.date), max(r.date)) from WorkRecordEntity r where r.dailyRate = de.adesso.budgeteer.common.old.MoneyUtil.ZERO and r.person.project.id = :projectId group by r.person.id, r.person.name")
  List<MissingDailyRateBean> getMissingDailyRatesForProject(@Param("projectId") long projectId);

  @Query(
      "select new de.adesso.budgeteer.persistence.record.MissingDailyRateForBudgetBean(r.person.id, r.person.name, min(r.date), max(r.date), b.name, b.id) from WorkRecordEntity r join r.budget b where r.dailyRate = de.adesso.budgeteer.common.old.MoneyUtil.ZERO and r.person.id = :personId group by r.person.id, r.person.name, b.name, b.id")
  List<MissingDailyRateForBudgetBean> getMissingDailyRatesForPerson(
      @Param("personId") long personId);

  @Override
  @Modifying
  @Query(
      "update WorkRecordEntity r set r.dailyRate = :dailyRate where r.editedManually = false AND r.budget.id=:budgetId and r.person.id=:personId and r.date between :fromDate and :toDate")
  void updateDailyRates(
      @Param("budgetId") long budgetId,
      @Param("personId") long personId,
      @Param("fromDate") Date fromDate,
      @Param("toDate") Date toDate,
      @Param("dailyRate") Money dailyRate);

  @Query(
      "select new de.adesso.budgeteer.persistence.record.DailyAverageRateBean(r.year, r.month, r.day, avg(r.dailyRate)) from WorkRecordEntity r where r.budget.project.id = :projectId and r.date >= :startDate group by r.year, r.month, r.day order by r.year, r.month, r.day")
  List<DailyAverageRateBean> getAverageDailyRatesPerDay(
      @Param("projectId") long projectId, @Param("startDate") Date startDate);

  @Override
  @Query("select count (wre.id) from WorkRecordEntity wre where wre.budget.project.id = :projectId")
  Long countByProjectId(@Param("projectId") long projectId);

  @Query(
      "select wr from WorkRecordEntity wr where wr.budget.project.id = :projectId AND wr.editedManually = true AND wr.date >= :startDate AND wr.date <= :endDate")
  List<WorkRecordEntity> findManuallyEditedEntries(
      @Param("projectId") long projectId,
      @Param("startDate") Date earliestRecordDate,
      @Param("endDate") Date latestRecordDate);

  @Query(
      "select wr from WorkRecordEntity wr where wr.budget = :budget AND wr.person = :person AND wr.date = :recordDate AND wr.minutes = :workedMinutes AND wr.editedManually = false")
  List<WorkRecordEntity> findDuplicateEntries(
      @Param("budget") BudgetEntity budget,
      @Param("person") PersonEntity person,
      @Param("recordDate") Date recordDate,
      @Param("workedMinutes") int workedMinutes);
}
