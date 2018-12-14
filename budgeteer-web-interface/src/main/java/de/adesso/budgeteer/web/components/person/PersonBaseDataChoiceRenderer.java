package de.adesso.budgeteer.web.components.person;

import de.adesso.budgeteer.service.person.PersonBaseData;
import de.adesso.budgeteer.web.pages.base.AbstractChoiceRenderer;

public class PersonBaseDataChoiceRenderer extends AbstractChoiceRenderer<PersonBaseData> {

    @Override
    public Object getDisplayValue(PersonBaseData object) {
        return object.getName();
    }
}
