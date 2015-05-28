package org.wickedsource.budgeteer.persistence.contract;


import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContractRepository extends CrudRepository<ContractEntity, Long>{

    List<ContractEntity> findByProjectId(long projectId);
}
