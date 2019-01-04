package de.adesso.budgeteer.service.project;

import de.adesso.budgeteer.persistence.project.ProjectEntity;
import de.adesso.budgeteer.service.AbstractMapper;
import org.springframework.stereotype.Component;

@Component
public class ProjectBaseDataMapper extends AbstractMapper<ProjectEntity, ProjectBaseData> {

    @Override
    public ProjectBaseData map(ProjectEntity entity) {
        if(entity != null){
            ProjectBaseData project = new ProjectBaseData();
            project.setId(entity.getId());
            project.setName(entity.getName());
            return project;
        }else{
            return null;
        }
    }
}
