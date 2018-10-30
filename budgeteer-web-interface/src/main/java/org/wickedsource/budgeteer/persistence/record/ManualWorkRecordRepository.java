package org.wickedsource.budgeteer.persistence.record;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;


public interface ManualWorkRecordRepository extends CrudRepository<ManualWorkRecordEntity, Long>,
        QueryDslPredicateExecutor<ManualWorkRecordEntity>, JpaSpecificationExecutor {

    }