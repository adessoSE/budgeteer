package de.adesso.budgeteer.rest.project;

import de.adesso.budgeteer.core.project.domain.Project;
import de.adesso.budgeteer.rest.project.model.ProjectModel;
import org.springframework.stereotype.Component;

@Component
public class ProjectModelMapper {
    public ProjectModel toModel(Project project) {
        return new ProjectModel(project.getId(), project.getName());
    }
}
