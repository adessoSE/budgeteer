package org.wickedsource.budgeteer.web.usecase.people.edit;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListView;
import org.wickedsource.budgeteer.service.notification.Notification;
import org.wickedsource.budgeteer.web.usecase.people.edit.component.form.EditPersonForm;

import java.io.Serializable;

/**
 * Strategy for the EditPersonPage, which can either be used to UPDATE an existing person or to CREATE a new person.
 */
public interface IEditPersonPageStrategy extends Serializable {

    Label createPageTitleLabel(String id);

    Label createSubmitButtonLabel(String id);

    ListView<Notification> createNotificationListView(String id, long personId);

    EditPersonForm createForm(String id, long personId);

    Link createBacklink(String id);

    void goBack();
}
