package org.wickedsource.budgeteer.persistence.invoice;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends CrudRepository<InvoiceEntity, Long> {

    @Query("Select ie from InvoiceEntity ie where ie.contract.project.id = :projectID")
    List<InvoiceEntity> findByProjectId(@Param("projectID") long projectId);

    List<InvoiceEntity> findByContractId(long contractId);

    @Modifying
    @Query("delete from InvoiceEntity ie where ie.id in (select id from InvoiceEntity where contract.project.id = :projectId)")
    void deleteByProjectId(@Param(value = "projectId") long projectId);

    @Modifying
    @Query("delete from ContractInvoiceField where id in (select id from ContractInvoiceField where contract.project.id = :projectId)")
    void deleteContractInvoiceFieldByProject(@Param("projectId") long projectId);

    @Modifying
    @Query("delete from InvoiceFieldEntity cif where cif.id in (select id from InvoiceFieldEntity where field.contract.project.id = :projectId )")
    void deleteInvoiceFieldByProjectId(@Param("projectId") long projectID);

    @Query("Select ie from InvoiceFieldEntity ie where ie.id = :id")
    InvoiceFieldEntity findInvoiceFieldById(@Param("id") long id);

    @Modifying
    @Query("delete from InvoiceFieldEntity cif where cif.id in (select id from InvoiceFieldEntity where field.contract.id = :contractId )")
    void deleteInvoiceFieldsByContractId(@Param("contractId") long contractId);

    @Modifying
    @Query("delete from InvoiceEntity cif where cif.contract.id  = :contractId")
    void deleteInvoicesByContractId(@Param("contractId") long contractId);

}
