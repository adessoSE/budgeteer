package org.wickedsource.budgeteer.web.pages.dashboard;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.web.BudgeteerSession;

public class ProjectnameModel extends AbstractReadOnlyModel<String> {

	@SpringBean private ProjectService projectService;

	public ProjectnameModel() {
		Injector.get().inject(this);
	}

	@Override
	public String getObject() {
		long projectId = BudgeteerSession.get().getProjectId();
		return projectService.findProjectById(projectId).getName();
	}
}
