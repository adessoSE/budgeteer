package org.wickedsource.budgeteer.persistence.template;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TemplateRepository extends CrudRepository<TemplateEntity, Long> {
	List<TemplateEntity> findByProjectId(long projectId);
}