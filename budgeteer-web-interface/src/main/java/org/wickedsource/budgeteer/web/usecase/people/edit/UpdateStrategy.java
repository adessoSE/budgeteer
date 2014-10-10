package org.wickedsource.budgeteer.web.usecase.people.edit;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.notification.Notification;
import org.wickedsource.budgeteer.service.people.PeopleService;
import org.wickedsource.budgeteer.service.people.PersonWithRates;
import org.wickedsource.budgeteer.web.usecase.people.edit.component.form.EditPersonForm;
import org.wickedsource.budgeteer.web.usecase.people.edit.component.notificationlist.PersonNotificationList;
import org.wickedsource.budgeteer.web.usecase.people.edit.component.notificationlist.PersonNotificationModel;

/**
 * Strategy to be used by EditPersonPage to UPDATE an existing person.
 */
public class UpdateStrategy implements IEditPersonPageStrategy {

    @SpringBean
    private PeopleService service;

    private EditPersonPage page;

    public UpdateStrategy(EditPersonPage page) {
        Injector.get().inject(this);
        this.page = page;
    }

    @Override
    public Label createPageTitleLabel(String id) {
        return new Label(id, new StringResourceModel("page.title.editmode", page, null));
    }

    @Override
    public Label createSubmitButtonLabel(String id) {
        return new Label(id, new StringResourceModel("button.save.editmode", page, null));
    }

    @Override
    public ListView<Notification> createNotificationListView(String id, long personId) {
        return new PersonNotificationList(id, new PersonNotificationModel(personId));
    }

    @Override
    public EditPersonForm createForm(String id, long personId) {
        PersonWithRates person = service.loadPersonWithRates(personId);
        return new EditPersonForm(id, person);
    }
}
