package org.wickedsource.budgeteer.persistence.contract;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContractRepository extends CrudRepository<ContractEntity, Long>{

    List<ContractEntity> findByProjectId(long projectId);

    @Query("select cif from ContractInvoiceField cif where cif.contract.id = :contractId AND cif.fieldName = :fieldName")
    public ContractInvoiceField findInvoiceFieldByName(@Param("contractId") long contractID, @Param("fieldName") String fieldName);


    /**
     * returns a ContractStatisticBean for a given contract till the given month and year.
     * returns the remaining budget of the contract, the spend budget in budgeteer and the invoiced budget until the given date
     */
    @Query("select new org.wickedsource.budgeteer.persistence.contract.ContractStatisticBean(:year+0," +
    		"(select coalesce(sum(wr.minutes * wr.dailyRate/ 60 / 8),0)" +
    		" from WorkRecordEntity wr where wr.budget.contract.id = :contractId AND(wr.year < :year OR (wr.year = :year AND wr.month <= :month))) / cast(c.budget AS double), " +
            "(c.budget - coalesce((select sum(wr.minutes * wr.dailyRate/ 60 / 8) " +
            "from WorkRecordEntity wr where wr.budget.contract.id = :contractId " +
            "AND (wr.year < :year OR (wr.year = :year AND wr.month <= :month))" +
            "),0l)" +
            ")," +
            "coalesce((select sum(wr.minutes * wr.dailyRate/ 60 / 8)" +
            "from WorkRecordEntity wr where wr.budget.contract.id = :contractId "+
            "AND (wr.year < :year OR (wr.year = :year AND wr.month <= :month))" +
            "),0l)," +
            "coalesce((select sum(i.invoiceSum) from InvoiceEntity i where i.contract.id = :contractId AND (i.year < :year OR (i.year = :year AND i.month <= :month) )),0l)" +
            ",:month +0" +
            ") from ContractEntity c where c.id = :contractId")
    public ContractStatisticBean getContractStatisticAggregatedByMonthAndYear(@Param("contractId") Long contractId, @Param("month") Integer month, @Param("year") Integer year);

    /**
     * returns a ContractStatisticBean for a given contract till the given month and year.
     * returns the remaining budget of the contract, the spend budget in budgeteer and the invoiced budget for the given month
     */
    @Query("select new org.wickedsource.budgeteer.persistence.contract.ContractStatisticBean(:year+0," +
    		"(select coalesce(sum(wr.minutes * wr.dailyRate/ 60 / 8),0)" +
    		" from WorkRecordEntity wr where wr.budget.contract.id = :contractId AND (wr.year < :year OR (wr.year = :year AND wr.month <= :month))) / cast(c.budget AS double), " +
            "(c.budget - coalesce((select sum(wr.minutes * wr.dailyRate/ 60 / 8) " +
            "from WorkRecordEntity wr where wr.budget.contract.id = :contractId " +
            "AND (wr.year = :year AND wr.month = :month)" +
            "),0l)" +
            ")," +
            "coalesce((select sum(wr.minutes * wr.dailyRate/ 60 / 8)" +
            "from WorkRecordEntity wr where wr.budget.contract.id = :contractId "+
            "AND (wr.year = :year AND wr.month = :month)" +
            "),0l)," +
            "coalesce((select sum(i.invoiceSum) from InvoiceEntity i where i.contract.id = :contractId AND (i.year = :year AND i.month = :month) ),0l)" +
            ",:month +0" +
            ") from ContractEntity c where c.id = :contractId")
    public ContractStatisticBean getContractStatisticByMonthAndYear(@Param("contractId") Long contractId, @Param("month") Integer month, @Param("year") Integer year);

    @Query("select coalesce(sum(wr.minutes * wr.dailyRate/ 60 / 8),0) from WorkRecordEntity wr where wr.budget.contract.id = :contractId")
    public Double getSpentBudgetByContractId(@Param("contractId") long contractId);

    @Query("select (c.budget - coalesce((select sum(wr.minutes * wr.dailyRate)/ 60 / 8 from WorkRecordEntity wr where wr.budget.contract.id = :contractId) ,0)) from ContractEntity c where c.id = :contractId")
    public Double getBudgetLeftByContractId(@Param("contractId") long contractId);
    
    @Modifying
    @Query("delete from ContractEntity c where c.project.id = :projectId")
    void deleteByProjectId(@Param(value = "projectId") long projectId);

    @Query("select c from ContractEntity c join fetch c.invoiceFields where c.id = :id")
    public ContractEntity findById(@Param("id") long id);

    @Modifying
    @Query("delete from ContractFieldEntity c where c.id in (select s.id from ContractFieldEntity s where  s.field.project.id = :projectId)")
    void deleteContractFieldByProjectId(@Param("projectId") long projectId);

    @Query("Select e from ContractFieldEntity e where e.contract.id = :contractID")
    List<ContractFieldEntity> findContractFieldsByContractId(@Param("contractID") Long contractID);
}