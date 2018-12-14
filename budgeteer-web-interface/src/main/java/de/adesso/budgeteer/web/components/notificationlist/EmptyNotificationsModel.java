package de.adesso.budgeteer.web.components.notificationlist;

import de.adesso.budgeteer.service.notification.Notification;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.List;

public class EmptyNotificationsModel implements IModel<List<Notification>> {
    @Override
    public List<Notification> getObject() {
        return new ArrayList<>();
    }

    @Override
    public void setObject(List<Notification> object) {
    }

    @Override
    public void detach() {
    }
}
