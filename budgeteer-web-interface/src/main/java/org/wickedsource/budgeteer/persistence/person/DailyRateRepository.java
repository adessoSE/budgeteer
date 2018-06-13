package org.wickedsource.budgeteer.persistence.person;

import java.util.Date;
import java.util.List;

import org.joda.money.Money;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DailyRateRepository extends CrudRepository<DailyRateEntity, Long> {

	@Query("select distinct r.rate from DailyRateEntity r where r.budget.project.id=:projectId")
	List<Money> getDistinctRatesInCents(@Param("projectId") long projectId);

	@Query(
			"select distinct r from DailyRateEntity r join fetch r.budget join fetch r.person where r.budget.project.id = :projectId")
	List<DailyRateEntity> findByProjectIdFetch(@Param("projectId") long ProjectId);

	DailyRateEntity findByBudgetIdAndPersonId(long budgetId, long personId);

	@Query(
			"select r from DailyRateEntity r where r.budget.id = :budgetId and r.person.id = :personId and ((:rangeStart between r.dateStart and r.dateEnd) or (:rangeEnd between r.dateStart and r.dateEnd))")
	DailyRateEntity findByBudgetAndPersonInDateRange(
			@Param("budgetId") long budgetId,
			@Param("personId") long personId,
			@Param("rangeStart") Date earliestDate,
			@Param("rangeEnd") Date latestDate);

	@Query(
			"select r from DailyRateEntity r where r.budget.id = :budgetId and r.person.id = :personId and "
					+ "((r.dateStart between :rangeStart and :rangeEnd) OR (r.dateEnd between :rangeStart and :rangeEnd) OR "
					+ "((:rangeStart between r.dateStart and r.dateEnd) AND (:rangeEnd between r.dateStart and r.dateEnd)))")
	List<DailyRateEntity> findByBudgetAndPersonWithOverlappingDateRange(
			@Param("budgetId") long budgetId,
			@Param("personId") long personId,
			@Param("rangeStart") Date earliestDate,
			@Param("rangeEnd") Date latestDate);

	@Query(
			"select r from DailyRateEntity r where r.budget.id = :budgetId and r.person.id = :personId and r.dateEnd >= :rangeStart")
	List<DailyRateEntity> findByBudgetAndPersonEndingInOrAfterDateRange(
			@Param("budgetId") long budgetId,
			@Param("personId") long personId,
			@Param("rangeStart") Date earliestDate);

	@Modifying
	@Query(
			"delete from DailyRateEntity r where r.budget.id in ( select b.id from BudgetEntity b where b.project.id = :projectId)")
	void deleteByProjectId(@Param("projectId") long projectId);
}
