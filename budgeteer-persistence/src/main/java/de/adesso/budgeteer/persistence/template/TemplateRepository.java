package de.adesso.budgeteer.persistence.template;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends CrudRepository<TemplateEntity, Long> {
  List<TemplateEntity> findByProjectId(long projectId);
}
