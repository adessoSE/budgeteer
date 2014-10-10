package org.wickedsource.budgeteer.web.usecase.people.edit;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListView;
import org.wickedsource.budgeteer.service.notification.Notification;
import org.wickedsource.budgeteer.web.usecase.people.edit.component.form.EditPersonForm;

import java.io.Serializable;

/**
 * Strategy for the EditPersonPage, which can either be used to UPDATE an existing person or to CREATE a new person.
 */
public interface IEditPersonPageStrategy extends Serializable {

    public Label createPageTitleLabel(String id);

    public Label createSubmitButtonLabel(String id);

    public ListView<Notification> createNotificationListView(String id, long personId);

    public EditPersonForm createForm(String id, long personId);

}
