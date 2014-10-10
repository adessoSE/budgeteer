package org.wickedsource.budgeteer.web.usecase.people.edit;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.usecase.base.FormPage;

@Mount({"people/edit/${id}", "people/edit"})
public class EditPersonPage extends FormPage {

    private IEditPersonPageStrategy strategy;

    /**
     * Use this constructor to create a page with a form to edit an existing user.
     *
     * @param parameters page parameters containing the id of the user to edit.
     */
    public EditPersonPage(PageParameters parameters) {
        super(parameters);
        strategy = new CreateStrategy(this);
        addComponents();
    }

    /**
     * Use this constructor to create a page with a form to create a new user.
     */
    public EditPersonPage() {
        strategy = new UpdateStrategy(this);
        addComponents();
    }

    private void addComponents() {
        add(strategy.createPageTitleLabel("pageTitle"));
        add(strategy.createSubmitButtonLabel("submitButtonTitle"));
        add(strategy.createNotificationListView("notificationList", getPersonId()));
        add(strategy.createForm("form", getPersonId()));
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

    private long getPersonId() {
        StringValue value = getPageParameters().get("id");
        if (value == null || value.isEmpty() || value.isNull()) {
            return 0l;
        } else {
            return value.toLong();
        }
    }

}
