package de.adesso.budgeteer.web.pages.user.selectproject;

import de.adesso.budgeteer.service.project.ProjectBaseData;
import de.adesso.budgeteer.web.pages.base.AbstractChoiceRenderer;

public class ProjectChoiceRenderer extends AbstractChoiceRenderer<ProjectBaseData> {

    @Override
    public Object getDisplayValue(ProjectBaseData object) {
        return object.getName();
    }
}
