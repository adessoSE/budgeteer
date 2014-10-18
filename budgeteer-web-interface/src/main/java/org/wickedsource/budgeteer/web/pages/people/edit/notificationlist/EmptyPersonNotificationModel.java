package org.wickedsource.budgeteer.web.pages.people.edit.notificationlist;

import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.notification.Notification;

import java.util.ArrayList;
import java.util.List;

public class EmptyPersonNotificationModel implements IModel<List<Notification>> {
    @Override
    public List<Notification> getObject() {
        return new ArrayList<Notification>();
    }

    @Override
    public void setObject(List<Notification> object) {
    }

    @Override
    public void detach() {
    }
}
