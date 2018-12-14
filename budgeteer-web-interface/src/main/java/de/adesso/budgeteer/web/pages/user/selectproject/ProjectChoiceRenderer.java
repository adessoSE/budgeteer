package de.adesso.budgeteer.web.pages.user.selectproject;

import de.adesso.budgeteer.web.pages.base.AbstractChoiceRenderer;
import de.adesso.budgeteer.service.project.ProjectBaseData;

public class ProjectChoiceRenderer extends AbstractChoiceRenderer<ProjectBaseData> {

    @Override
    public Object getDisplayValue(ProjectBaseData object) {
        return object.getName();
    }
}
