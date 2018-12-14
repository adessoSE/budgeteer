package de.adesso.budgeteer.web.pages.dashboard;

import de.adesso.budgeteer.service.project.ProjectService;
import de.adesso.budgeteer.web.BudgeteerSession;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ProjectnameModel extends AbstractReadOnlyModel<String> {

    @SpringBean
    private ProjectService projectService;

    public ProjectnameModel() {
        Injector.get().inject(this);
    }

    @Override
    public String getObject() {
        long projectId = BudgeteerSession.get().getProjectId();
        return projectService.findProjectById(projectId).getName();
    }
}
