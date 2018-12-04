package org.wickedsource.budgeteer.persistence.fixedDailyRate;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;


public interface FixedDailyRateRepository extends QueryDslPredicateExecutor<FixedDailyRateEntity>,
        CrudRepository<FixedDailyRateEntity, Long>, JpaSpecificationExecutor {
    @Query("select new FixedDailyRateEntity(r.id,  r.budget, r.startDate, r.endDate, r.moneyAmount, r.name, r.description) " +
            "from FixedDailyRateEntity r where r.budget.id = :budgetId")
    List<FixedDailyRateEntity> getFixedDailyRateByBudgetId(@Param("budgetId") long budgetId);
}
