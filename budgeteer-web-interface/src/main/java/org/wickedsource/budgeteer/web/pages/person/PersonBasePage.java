package org.wickedsource.budgeteer.web.pages.person;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;

public abstract class PersonBasePage extends BasePage {

    public PersonBasePage(PageParameters parameters){
        super(parameters);
    }

    /**
     * Creates a valid PageParameters object to pass into the constructor of this page class.
     *
     * @param personId id of the person whose details to display.
     * @return a valid PageParameters object.
     */
    public static PageParameters createParameters(long personId) {
        PageParameters parameters = new PageParameters();
        parameters.add("id", personId);
        return parameters;
    }

    protected long getPersonId() {
        return getPageParameters().get("id").toLong();
    }

}
