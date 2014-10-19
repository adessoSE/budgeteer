package org.wickedsource.budgeteer.web.pages.base.basepage.notifications;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.notification.Notification;
import org.wickedsource.budgeteer.service.notification.NotificationService;
import org.wickedsource.budgeteer.service.notification.NotificationType;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.PropertyLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

public class NotificationDropdownTest extends AbstractWebTestTemplate {

    @Autowired
    NotificationService service;

    @Test
    public void testRenderFull() {
        WicketTester tester = getTester();
        when(service.getNotifications(1l)).thenReturn(createNotifications());
        NotificationModel model = new NotificationModel(1l);
        NotificationDropdown dropdown = new NotificationDropdown("dropdown", model);
        tester.startComponentInPage(dropdown);

        tester.assertContains(PropertyLoader.getProperty(NotificationDropdown.class, "header.whenFull"));
        tester.assertLabel("dropdown:notificationCountLabel", "2");
    }

    @Test
    public void testRenderEmpty() {
        WicketTester tester = getTester();
        when(service.getNotifications(1l)).thenReturn(new ArrayList<Notification>());
        NotificationModel model = new NotificationModel(1l);
        NotificationDropdown dropdown = new NotificationDropdown("dropdown", model);
        tester.startComponentInPage(dropdown);

        tester.assertContains(PropertyLoader.getProperty(NotificationDropdown.class, "header.whenEmpty"));
        tester.assertInvisible("dropdown:notificationCountLabel");
        tester.assertInvisible("dropdown:dropdownMenu");
    }

    private List<Notification> createNotifications() {
        List<Notification> notifications = new ArrayList<Notification>();
        notifications.add(new Notification("Notification", NotificationType.MISSING_BUDGET_TOTAL, new Date()));
        notifications.add(new Notification("Notification", NotificationType.MISSING_DAILY_RATE, new Date()));
        return notifications;
    }

}
