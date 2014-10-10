package org.wickedsource.budgeteer.web.usecase.people.edit.component.notificationlist;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.notification.Notification;

import java.util.List;

public class PersonNotificationList extends ListView<Notification> {

    public PersonNotificationList(String id, IModel<? extends List<? extends Notification>> model) {
        super(id, model);
    }

    @Override
    protected void populateItem(ListItem<Notification> item) {
        item.add(new Label("text", item.getModelObject().getMessage()));
    }

}
