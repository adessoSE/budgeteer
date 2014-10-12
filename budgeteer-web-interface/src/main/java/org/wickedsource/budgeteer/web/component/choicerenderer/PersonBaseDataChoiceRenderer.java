package org.wickedsource.budgeteer.web.component.choicerenderer;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.wickedsource.budgeteer.service.people.PersonBaseData;

public class PersonBaseDataChoiceRenderer implements IChoiceRenderer<PersonBaseData> {

    @Override
    public Object getDisplayValue(PersonBaseData object) {
        return object.getName();
    }

    @Override
    public String getIdValue(PersonBaseData object, int index) {
        return String.valueOf(object.getId());
    }
}
