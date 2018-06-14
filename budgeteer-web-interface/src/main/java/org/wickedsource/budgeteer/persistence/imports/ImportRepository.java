package org.wickedsource.budgeteer.persistence.imports;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImportRepository extends CrudRepository<ImportEntity, Long> {

    public List<ImportEntity> findByProjectId(long projectId);

    @Modifying
    @Query("delete from ImportEntity i where i.project.id = :projectId")
    public void deleteByProjectId(@Param("projectId") long projectId);

}
