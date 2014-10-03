package org.wickedsource.budgeteer.components.notification;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wickedsource.budgeteer.service.notification.Notification;
import org.wickedsource.budgeteer.service.notification.NotificationService;
import org.wickedsource.budgeteer.service.notification.NotificationType;
import org.wickedsource.budgeteer.web.base.BudgeteerApplication;
import org.wickedsource.budgeteer.web.components.PropertyLoader;
import org.wickedsource.budgeteer.web.components.notification.NotificationDropdown;
import org.wickedsource.budgeteer.web.components.notification.NotificationModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-web.xml", "classpath:spring-service-mock.xml"})
public class NotificationDropdownTest {

    @Autowired
    BudgeteerApplication application;

    @Autowired
    NotificationService service;

    private static WicketTester tester;

    private WicketTester getTester() {
        if (tester == null) {
            tester = new WicketTester(application);
        }
        return tester;
    }

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
