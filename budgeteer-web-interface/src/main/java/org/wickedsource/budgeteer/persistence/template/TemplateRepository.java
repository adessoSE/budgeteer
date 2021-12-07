package org.wickedsource.budgeteer.persistence.template;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepository extends CrudRepository<TemplateEntity, Long> {
    List<TemplateEntity> findByProjectId(long projectId);
}