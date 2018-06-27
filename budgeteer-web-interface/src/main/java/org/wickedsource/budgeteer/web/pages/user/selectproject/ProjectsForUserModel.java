package org.wickedsource.budgeteer.web.pages.user.selectproject;

import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.project.ProjectBaseData;
import org.wickedsource.budgeteer.service.project.ProjectService;

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
