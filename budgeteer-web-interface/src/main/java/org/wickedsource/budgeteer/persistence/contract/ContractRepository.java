package org.wickedsource.budgeteer.persistence.contract;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContractRepository extends CrudRepository<ContractEntity, Long>{

    List<ContractEntity> findByProjectId(long projectId);

    @Query("select cif from ContractInvoiceField cif where cif.contract.id = :contractId AND cif.fieldName = :fieldName")
    public ContractInvoiceField findInvoiceFieldByName(@Param("contractId") long contractID, @Param("fieldName") String fieldName);
}
