package de.adesso.budgeteer.web.components.notificationlist;

import de.adesso.budgeteer.service.notification.Notification;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.List;

public class NotificationListPanel extends Panel {

    private NotificationMessageFactory notificationMessageFactory = new NotificationMessageFactory();

    public NotificationListPanel(String id, IModel<? extends List<Notification>> model) {
        super(id, model);
        setRenderBodyOnly(true);
        add(createNotificationList("notificationList", model));
    }

    private ListView<Notification> createNotificationList(String id, IModel<? extends List<Notification>> model) {
        return new ListView<Notification>(id, model) {
            @Override
            protected void populateItem(ListItem<Notification> item) {
                item.add(new Label("text", notificationMessageFactory.getMessageForNotification(item.getModelObject())));
            }
        };
    }
}
