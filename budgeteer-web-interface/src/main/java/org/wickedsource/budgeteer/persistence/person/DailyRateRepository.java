package org.wickedsource.budgeteer.persistence.person;

import org.joda.money.Money;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DailyRateRepository extends CrudRepository<DailyRateEntity, Long> {

    @Query("select distinct r.rate from DailyRateEntity r where r.budget.project.id=:projectId")
    List<Money> getDistinctRatesInCents(@Param("projectId") long projectId);

}
