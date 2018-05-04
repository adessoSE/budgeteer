package org.wickedsource.budgeteer.persistence.template;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TemplateRepository extends CrudRepository<TemplateEntity, Long> {
    List<TemplateEntity> findByProjectId(long projectId);
}