package org.wickedsource.budgeteer.service.project;

import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectBaseDataMapper {

    public List<ProjectBaseData> toDTO(List<ProjectEntity> entities) {
        List<ProjectBaseData> result = new ArrayList<ProjectBaseData>();
        for (ProjectEntity entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }

    public ProjectBaseData toDTO(ProjectEntity entity) {
        ProjectBaseData project = new ProjectBaseData();
        project.setId(entity.getId());
        project.setName(entity.getName());
        return project;
    }

}
