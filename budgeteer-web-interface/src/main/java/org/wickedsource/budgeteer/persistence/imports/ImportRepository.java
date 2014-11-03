package org.wickedsource.budgeteer.persistence.imports;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ImportRepository extends CrudRepository<ImportEntity, Long> {

    public List<ImportEntity> findByProjectId(long projectId);

}
