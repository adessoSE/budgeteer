package org.wickedsource.budgeteer.service.project;

import de.adesso.budgeteer.persistence.project.ProjectEntity;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.service.AbstractMapper;

@Component
public class ProjectBaseDataMapper extends AbstractMapper<ProjectEntity, ProjectBaseData> {

  @Override
  public ProjectBaseData map(ProjectEntity entity) {
    ProjectBaseData project = new ProjectBaseData();
    project.setId(entity.getId());
    project.setName(entity.getName());
    return project;
  }
}
