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

    @Query("select new org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRate(r.id, r.budget.id, r.moneyAmount, r.startDate, r.endDate, r.description, r.name, r.budget.contract.taxRate, r.days) " +
            "from FixedDailyRateEntity r " +
            "where r.budget.id = :budgetId")
    List<FixedDailyRate> getFixedDailyRatesByBudgetId(@Param("budgetId") long budgetId);

    @Query("select new org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRate(r.id, r.budget.id, r.moneyAmount, r.startDate, r.endDate, r.description, r.name, r.budget.contract.taxRate, r.days) " +
            "from FixedDailyRateEntity r " +
            "where r.budget.id = :budgetId and r.endDate >= :startDate")
    List<FixedDailyRate> getFixedDailyRatesByBudgetId(@Param("budgetId") long budgetId, @Param("startDate") Date start);

    @Query("select new org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRate(r.id, r.budget.id, r.moneyAmount, r.startDate, r.endDate, r.description, r.name, r.budget.contract.taxRate, r.days) " +
            "from FixedDailyRateEntity r " +
            "where r.budget.project.id = :projectId ")
    List<FixedDailyRate> getFixedDailyRatesByProjectId(@Param("projectId") long projectId);

    @Query("select new org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRate(f.id, f.budget.id, f.moneyAmount, f.startDate, f.endDate, f.description, f.name, f.budget.contract.taxRate, f.days) " +
            "from FixedDailyRateEntity f " +
            "where f.budget.project.id=:projectId and f.endDate >= :startDate")
    List<FixedDailyRate> getFixedDailyRatesByProjectIdAndStartDate(@Param("projectId") long projectId, @Param("startDate") Date start);

    @Query("select distinct new org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRate(r.id, b.id, r.moneyAmount, r.startDate, r.endDate, r.description, r.name, b.contract.taxRate, r.days) " +
            "from FixedDailyRateEntity r " +
            "join r.budget b join b.tags t join b.project p where p.id=:projectId and t.tag in (:tags) ")
    List<FixedDailyRate> getFixedDailyRatesByProjectIdAndTags(@Param("projectId") long projectId, @Param("tags") List<String> tags);

    @Query("select distinct new org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRate(r.id, b.id, r.moneyAmount, r.startDate, r.endDate, r.description, r.name, b.contract.taxRate, r.days) " +
            "from FixedDailyRateEntity r " +
            "join r.budget b join b.tags t join b.project p where p.id=:projectId and t.tag in (:tags) and r.endDate >= :startDate")
    List<FixedDailyRate> getFixedDailyRatesByProjectIdAndTags(@Param("projectId") long projectId, @Param("tags") List<String> tags, @Param("startDate") Date start);

    @Query("select new org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRate(r.id, r.budget.id, r.moneyAmount, r.startDate, r.endDate, r.description, r.name, r.budget.contract.taxRate, r.days) " +
            "from FixedDailyRateEntity r " +
            "where r.budget.contract.id=:contractId")
    List<FixedDailyRate> getFixedDailyRatesByContract(@Param("contractId") long contractId);
}
