package org.wickedsource.budgeteer.persistence.fixedDailyRate;

import org.joda.money.Money;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordWithTitleAndTaxBean;

import java.io.Serializable;
import java.util.List;


public interface FixedDailyRateRepository extends QueryDslPredicateExecutor<FixedDailyRateEntity>,
        CrudRepository<FixedDailyRateEntity, Long>, JpaSpecificationExecutor {
    @Query("select new FixedDailyRateEntity(r.id,  r.budget, r.startDate, r.endDate, r.days, r.moneyAmount, r.name, r.description) " +
            "from FixedDailyRateEntity r where r.budget.id = :budgetId")
    List<FixedDailyRateEntity> getFixedDailyRateByBudgetId(@Param("budgetId") long budgetId);

    @Query("select r.endDate - r.startDate from FixedDailyRateEntity r where r.id = :rateId")
    int getDaysOfFixedDailyRate(@Param("rateId") long rateId);

    @Query("select coalesce(sum(fixed.moneyAmount*fixed.days),0) from FixedDailyRateEntity fixed where fixed.budget.id = :budgetId")
    Double getFixedDailyRateAmountOfBudget(@Param("budgetId") long budgetId);
}
