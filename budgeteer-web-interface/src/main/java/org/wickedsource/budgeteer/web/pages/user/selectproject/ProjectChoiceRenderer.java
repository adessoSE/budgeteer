package org.wickedsource.budgeteer.web.pages.user.selectproject;

import org.wickedsource.budgeteer.service.project.ProjectBaseData;
import org.wickedsource.budgeteer.web.pages.base.AbstractChoiceRenderer;

public class ProjectChoiceRenderer extends AbstractChoiceRenderer<ProjectBaseData> {

	@Override
	public Object getDisplayValue(ProjectBaseData object) {
		return object.getName();
	}
}
