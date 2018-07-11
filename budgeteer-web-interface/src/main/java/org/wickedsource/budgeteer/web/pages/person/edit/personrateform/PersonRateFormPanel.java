package org.wickedsource.budgeteer.web.pages.person.edit.personrateform;

import org.apache.wicket.markup.html.panel.Panel;

public class PersonRateFormPanel extends Panel {

    public PersonRateFormPanel(String id, PersonRateForm rateForm){
        super(id);
        add(rateForm);
    }
}
