package org.wickedsource.budgeteer.persistence.fixedDailyRate;

import org.joda.money.Money;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean;
import org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public interface FixedDailyRateRepository extends QueryDslPredicateExecutor<FixedDailyRateEntity>,
        CrudRepository<FixedDailyRateEntity, Long>, JpaSpecificationExecutor {
    @Query("select new FixedDailyRateEntity(r.id,  r.budget, r.startDate, r.endDate, r.days, r.moneyAmount, r.name, r.description) " +
            "from FixedDailyRateEntity r " +
            "where r.budget.id = :budgetId")
    List<FixedDailyRateEntity> getFixedDailyRateEntitesByBudgetId(@Param("budgetId") long budgetId);

    // ToDo Test
    @Query("select new org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRate(r.id, r.budget.id, r.moneyAmount, r.startDate, r.endDate, r.description, r.name, r.budget.contract.taxRate, r.days) " +
            "from FixedDailyRateEntity r " +
            "where r.budget.id = :budgetId")
    List<FixedDailyRate> getFixedDailyRatesByBudgetId(@Param("budgetId") long budgetId);

    // ToDo Test
    @Query("select new org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRate(r.id, r.budget.id, r.moneyAmount, r.startDate, r.endDate, r.description, r.name, r.budget.contract.taxRate, r.days) " +
            "from FixedDailyRateEntity r " +
            "where r.budget.project.id = :projectId " +
            "group by r.budget.contract.taxRate")
    List<FixedDailyRate> getFixedDailyRatesByProjectId(@Param("projectId") long projectId);

    @Query("select new org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRate(r.id, r.budget.id, r.moneyAmount, r.startDate, r.endDate, r.description, r.name, r.budget.contract.taxRate, r.days) " +
            "from FixedDailyRateEntity r" +
            "where r.budget.project.id=:projectId and r.startDate <= :today and r.endDate >= :startDate" +
            "group by r.year, r.week order by r.year, r.week")
    List<FixedDailyRate> getFixedDailyRatesByProjectIdAndStartDate(@Param("projectId") long projectId, @Param("startDate") Date start, @Param("today") Date today);

    //ToDo Test
    @Query("select new org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRate(r.id, b.id, r.moneyAmount, r.startDate, r.endDate, r.description, r.name, b.contract.taxRate, r.days) " +
            "from FixedDailyRateEntity r " +
            "join r.budget b join b.tags t where b.project.id=:projectId and t.tag in (:tags) " +
            "group by r.budget.contract.taxRate")
    List<FixedDailyRate> getFixedDailyRatesByProjectIdAndTags(@Param("projectId") long projectId, @Param("tags") List<String> tags);

    //long id, long budgetId, Money moneyAmount, Date startDate, Date endDate, String description, String name, BigDecimal taxRate, int days

    @Query("select r.endDate - r.startDate from FixedDailyRateEntity r where r.id = :rateId")
    int getDaysOfFixedDailyRate(@Param("rateId") long rateId);

    @Query("select coalesce(sum(fixed.moneyAmount*fixed.days),0) from FixedDailyRateEntity fixed where fixed.budget.id = :budgetId")
    Double getFixedDailyRateAmountOfBudget(@Param("budgetId") long budgetId);
}
