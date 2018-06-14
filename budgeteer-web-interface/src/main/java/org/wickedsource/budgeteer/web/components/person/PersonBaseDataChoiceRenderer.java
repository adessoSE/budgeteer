package org.wickedsource.budgeteer.web.components.person;

import org.wickedsource.budgeteer.service.person.PersonBaseData;
import org.wickedsource.budgeteer.web.pages.base.AbstractChoiceRenderer;

public class PersonBaseDataChoiceRenderer extends AbstractChoiceRenderer<PersonBaseData> {

    @Override
    public Object getDisplayValue(PersonBaseData object) {
        return object.getName();
    }
}
