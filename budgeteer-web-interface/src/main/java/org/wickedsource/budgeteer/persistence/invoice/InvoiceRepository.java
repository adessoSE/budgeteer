package org.wickedsource.budgeteer.persistence.invoice;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceRepository extends CrudRepository<InvoiceEntity, Long> {

    @Query("Select ie from InvoiceEntity ie where ie.contract.project.id = :projectID")
    List<InvoiceEntity> findByProjectId(@Param("projectID") long projectId);

    List<InvoiceEntity> findByContractId(long contractId);
}
