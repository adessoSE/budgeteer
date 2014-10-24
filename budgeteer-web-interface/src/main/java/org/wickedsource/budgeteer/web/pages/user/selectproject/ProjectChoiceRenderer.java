package org.wickedsource.budgeteer.web.pages.user.selectproject;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.wickedsource.budgeteer.service.project.ProjectBaseData;

public class ProjectChoiceRenderer implements IChoiceRenderer<ProjectBaseData> {

    @Override
    public Object getDisplayValue(ProjectBaseData object) {
        return object.getName();
    }

    @Override
    public String getIdValue(ProjectBaseData object, int index) {
        return String.valueOf(index);
    }

}
