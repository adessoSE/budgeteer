package de.adesso.budgeteer.web.pages.user.selectproject;

import de.adesso.budgeteer.service.project.ProjectBaseData;
import de.adesso.budgeteer.service.project.ProjectService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ProjectsForUserModel extends AbstractReadOnlyModel<List<ProjectBaseData>> {

    @SpringBean
    private ProjectService service;

    private long userId;

    public ProjectsForUserModel(long userId) {
        Injector.get().inject(this);
        this.userId = userId;
    }

    @Override
    public List<ProjectBaseData> getObject() {
        return service.getProjectsForUser(userId);
    }
}
