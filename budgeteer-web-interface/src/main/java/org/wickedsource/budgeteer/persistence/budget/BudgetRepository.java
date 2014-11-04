package org.wickedsource.budgeteer.persistence.budget;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BudgetRepository extends CrudRepository<BudgetEntity, Long> {

    @Query("select distinct t.tag from BudgetTagEntity t where t.budget.project.id = :projectId")
    public List<String> getAllTagsInProject(@Param("projectId") long projectId);

    public List<BudgetEntity> findByProjectId(long projectId);

    /**
     * Searches for Budgets that are tagged with AT LEAST ONE of the given tags.
     *
     * @param tags the tags to search for
     * @return all Budgets that match the search criteria
     */
    @Query("select distinct b from BudgetEntity b join b.tags t where t.tag in (:tags) and b.project.id=:projectId")
    public List<BudgetEntity> findByAtLeastOneTag(@Param("projectId") long projectId, @Param("tags") List<String> tags);

    @Query("select new org.wickedsource.budgeteer.persistence.budget.MissingBudgetTotal(b.id, b.name) from BudgetEntity b where b.total = 0 and b.project.id=:projectId")
    public List<MissingBudgetTotal> getMissingBudgetTotalsForProject(@Param("projectId") long projectId);

    /**
     * Returns a MissingBudgetTotal object for the given Budget if it's budget total is zero. Returns null, if the budget is not zero!
     */
    @Query("select new org.wickedsource.budgeteer.persistence.budget.MissingBudgetTotal(b.id, b.name) from BudgetEntity b where b.total = 0 and b.id=:budgetId")
    public MissingBudgetTotal getMissingBudgetTotalForBudget(@Param("budgetId") long budgetId);

}
