package de.adesso.budgeteer.persistence.contract;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends CrudRepository<ContractEntity, Long> {

  List<ContractEntity> findByProjectId(long projectId);

  boolean existsByIdAndProjectId(long contractId, long projectId);

  @Query(
      "select coalesce(sum(wr.minutes * wr.dailyRate/ 60 / 8),0) from WorkRecordEntity wr where wr.budget.contract.id = :contractId")
  Double getSpentBudgetByContractId(@Param("contractId") long contractId);

  @Query(
      "select (c.budget - coalesce((select sum(wr.minutes * wr.dailyRate)/ 60 / 8 from WorkRecordEntity wr where wr.budget.contract.id = :contractId) ,0)) from ContractEntity c where c.id = :contractId")
  Double getBudgetLeftByContractId(@Param("contractId") long contractId);

  @Modifying
  @Query("delete from ContractEntity c where c.project.id = :projectId")
  void deleteByProjectId(@Param(value = "projectId") long projectId);

  @Modifying
  @Query(
      "delete from ContractFieldEntity c where c.id in (select s.id from ContractFieldEntity s where  s.field.project.id = :projectId)")
  void deleteContractFieldByProjectId(@Param("projectId") long projectId);

  @Query("Select e from ContractFieldEntity e where e.contract.id = :contractID")
  List<ContractFieldEntity> findContractFieldsByContractId(@Param("contractID") Long contractID);

  @Query(
      "select coalesce(sum(wr.minutes * wr.dailyRate/ 60 / 8),0) from WorkRecordEntity wr where wr.budget.contract.id = :contractId AND (wr.year < :year OR (wr.year = :year AND wr.month <= :month))")
  Double getSpentBudgetByContractIdUntilDate(
      @Param("contractId") Long contractId,
      @Param("month") Integer month,
      @Param("year") Integer year);

  @Query(
      "select ( coalesce(sum(wr.minutes * wr.dailyRate/ 60 / 8),0)*"
          + "coalesce((select 1.0+c.taxRate/100.0 from ContractEntity c where c.id = :contractId),1.0)) "
          + "from WorkRecordEntity wr where wr.budget.contract.id = :contractId")
  Double getSpentBudgetGrossByContractId(@Param("contractId") Long contractId);
}
