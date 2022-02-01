package org.wickedsource.budgeteer.persistence.budget;

import java.util.List;
import org.joda.money.Money;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends CrudRepository<BudgetEntity, Long> {

  boolean existsByImportKeyAndProjectId(String importKey, long projectId);

  @Query(
      "SELECT (count(budget) > 0) FROM BudgetEntity budget WHERE budget.project.id = "
          + "(SELECT budget.project.id FROM BudgetEntity budget where budget.id = :budgetId)"
          + "AND budget.importKey = :importKey")
  boolean existsWithImportKeyInProjectByBudgetId(
      @Param("budgetId") long budgetId, @Param("importKey") String importKey);

  boolean existsByNameAndProjectId(String name, long projectId);

  @Query(
      "SELECT (count(budget) > 0) FROM BudgetEntity budget WHERE budget.project.id = "
          + "(SELECT budget.project.id FROM BudgetEntity budget where budget.id = :budgetId)"
          + "AND budget.name = :name")
  boolean existsWithNameInProjectByBudgetId(
      @Param("budgetId") long budgetId, @Param("name") String name);

  @Modifying
  @Query(
      "UPDATE BudgetEntity budget SET budget.contract = null WHERE budget.contract.id = :contractId")
  void removeReferencesToContract(@Param("contractId") long contractId);

  @Query("select distinct t.tag from BudgetTagEntity t where t.budget.project.id = :projectId")
  List<String> getAllTagsInProject(@Param("projectId") long projectId);

  List<BudgetEntity> findByProjectIdOrderByNameAsc(long projectId);

  @Query("SELECT budget.note FROM BudgetEntity budget WHERE budget.id = :budgetId")
  String getNoteForBudget(@Param("budgetId") long budgetId);

  @Modifying
  @Query("UPDATE BudgetEntity  budget SET budget.note = :note WHERE budget.id = :budgetId")
  void updateNoteForBudget(@Param("budgetId") long budgetId, @Param("note") String note);

  /**
   * Searches for Budgets that are tagged with AT LEAST ONE of the given tags.
   *
   * @param tags the tags to search for
   * @return all Budgets that match the search criteria
   */
  @Query(
      "select b from BudgetEntity b join b.tags t where t.tag in (:tags) and b.project.id=:projectId order by b.name")
  List<BudgetEntity> findByAtLeastOneTag(
      @Param("projectId") long projectId, @Param("tags") List<String> tags);

  @Query(
      "select new org.wickedsource.budgeteer.persistence.budget.MissingBudgetTotalBean(b.id, b.name) from BudgetEntity b where b.total = de.adesso.budgeteer.common.old.MoneyUtil.ZERO and b.project.id=:projectId order by b.name")
  List<MissingBudgetTotalBean> getMissingBudgetTotalsForProject(@Param("projectId") long projectId);

  /**
   * Returns a MissingBudgetTotalBean object for the given Budget if it's budget total is zero.
   * Returns null, if the budget is not zero!
   */
  @Query(
      "select new org.wickedsource.budgeteer.persistence.budget.MissingBudgetTotalBean(b.id, b.name) from BudgetEntity b where b.total = de.adesso.budgeteer.common.old.MoneyUtil.ZERO and b.id=:budgetId order by b.name")
  MissingBudgetTotalBean getMissingBudgetTotalForBudget(@Param("budgetId") long budgetId);

  @Query(
      "select new org.wickedsource.budgeteer.persistence.budget.LimitReachedBean(b.id, b.name) from BudgetEntity b where b.limit <= :spent and b.limit > de.adesso.budgeteer.common.old.MoneyUtil.ZERO and b.id=:budgetId order by b.name")
  LimitReachedBean getLimitReachedForBudget(
      @Param("budgetId") long budgetId, @Param("spent") Money spent);

  @Query(
      "select new org.wickedsource.budgeteer.persistence.budget.LimitReachedBean(b.id, b.name) from BudgetEntity b where b.project.id=:projectId order by b.name")
  List<LimitReachedBean> getBudgetsForProject(@Param("projectId") long projectId);

  @Query("select b.id from BudgetEntity b where b.contract is null and b.project.id=:projectId")
  List<Long> getMissingContractForProject(@Param("projectId") long projectId);

  @Modifying
  @Query("delete from BudgetEntity b where b.project.id = :projectId")
  void deleteByProjectId(@Param("projectId") long projectId);

  @Query("select b from BudgetEntity b where b.contract.id = :contractId")
  List<BudgetEntity> findByContractId(@Param("contractId") long cId);

  @Query("select wr.budget from WorkRecordEntity wr where wr.person.id = :personId")
  List<BudgetEntity> findByPersonId(@Param("personId") long personId);

  /**
   * Returns the TaxCoefficient defined by the contract of the budget. If the budget has no contract
   * or the contract no taxrate assigned, this method returns 1.0
   *
   * @param budgetId The primary key of the budget record at hand.
   * @return 1.0+taxRate/100
   */
  @Query(
      "SELECT 1.0 + coalesce((SELECT contract.taxRate FROM ContractEntity contract WHERE contract = budget.contract),0) /100.0 FROM BudgetEntity budget WHERE budget.id = :budgetId")
  Double getTaxCoefficientByBudget(@Param("budgetId") long budgetId);
}
