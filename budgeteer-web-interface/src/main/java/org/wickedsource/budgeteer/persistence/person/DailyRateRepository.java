package org.wickedsource.budgeteer.persistence.person;

import org.joda.money.Money;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DailyRateRepository extends CrudRepository<DailyRateEntity, Long> {

    @Query("select distinct r.rate from DailyRateEntity r where r.budget.project.id=:projectId")
    List<Money> getDistinctRatesInCents(@Param("projectId") long projectId);

    @Query("select distinct r from DailyRateEntity r join fetch r.budget join fetch r.person where r.budget.project.id = :projectId")
    List<DailyRateEntity> findByProjectIdFetch (@Param("projectId") long ProjectId);

}
