package org.wickedsource.budgeteer.persistence.budget;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface BudgetRepository extends CrudRepository<BudgetEntity, Long> {

    @Query("select elements(b.tags) from BudgetEntity b where b.project.id = :projectId")
    public Set<String> getAllTagsInProject(@Param("projectId") long projectId);

    public List<BudgetEntity> findByProjectId(long projectId);

}
