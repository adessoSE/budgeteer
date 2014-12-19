package org.wickedsource.budgeteer.web.pages.base.basepage.notifications;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.wickedsource.budgeteer.service.notification.Notification;
import org.wickedsource.budgeteer.web.components.notificationlist.NotificationLinkFactory;
import org.wickedsource.budgeteer.web.components.notificationlist.NotificationMessageFactory;

public class NotificationDropdown extends Panel {

    private NotificationLinkFactory notificationLinkFactory = new NotificationLinkFactory();

    private NotificationMessageFactory notificationMessageFactory = new NotificationMessageFactory();

    public NotificationDropdown(String id, NotificationModel model) {
        super(id, model);
        initNotification();
        initHeader();
        initMenu();
    }

    private void initMenu() {
        final ListView<Notification> listview = new ListView<Notification>("notificationList", getModel()) {
            @Override
            protected void populateItem(final ListItem<Notification> item) {
                Link link = new Link("notificationLink") {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onClick() {
                        setResponsePage(notificationLinkFactory.getLinkForNotification(item.getModelObject(), (Class<? extends WebPage>) getPage().getClass(), getPage().getPageParameters()));
                    }
                };
                item.add(link);

                Label messageLabel = new Label("notificationMessage", notificationMessageFactory.getMessageForNotification(item.getModelObject()));
                link.add(messageLabel);
            }
        };

        WebMarkupContainer menu = new WebMarkupContainer("dropdownMenu") {
            @Override
            public boolean isVisible() {
                return 0 != listview.getModelObject().size();
            }
        };
        menu.add(listview);

        add(menu);
    }

    private void initHeader() {
        add(new Label("dropdownHeader", getModel().getHeaderModel()));
    }

    private void initNotification() {
        add(new Label("notificationCountLabel", getModel().getNotificationCountModel()) {
            @Override
            public boolean isVisible() {
                return 0 != (Integer) getDefaultModelObject();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private NotificationModel getModel() {
        return (NotificationModel) getDefaultModel();
    }

}
