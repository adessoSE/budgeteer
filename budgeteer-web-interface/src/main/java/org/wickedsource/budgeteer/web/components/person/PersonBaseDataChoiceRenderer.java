package org.wickedsource.budgeteer.web.components.person;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.wickedsource.budgeteer.service.person.PersonBaseData;

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
