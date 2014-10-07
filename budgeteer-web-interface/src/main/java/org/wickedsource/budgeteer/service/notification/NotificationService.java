package org.wickedsource.budgeteer.service.notification;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NotificationService {

    /**
     * Returns all notifications currently available for the given user
     *
     * @param userId the ID of the user whose notifications to get
     * @return list of notifications
     */
    public List<Notification> getNotifications(long userId) {
        return createNotifications();
    }

    private List<Notification> createNotifications() {
        List<Notification> notifications = new ArrayList<Notification>();
        notifications.add(new Notification("Notification", NotificationType.MISSING_BUDGET_TOTAL, new Date()));
        notifications.add(new Notification("Notification", NotificationType.MISSING_DAILY_RATE, new Date()));
        return notifications;
    }
}
