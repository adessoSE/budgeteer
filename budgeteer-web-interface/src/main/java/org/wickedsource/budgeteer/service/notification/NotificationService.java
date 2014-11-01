package org.wickedsource.budgeteer.service.notification;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class NotificationService {

    /**
     * Returns all notifications currently available for the given user
     *
     * @param projectId ID of the project whose notificationsto load
     * @return list of notifications
     */
    public List<Notification> getNotifications(long projectId) {
        return createNotifications();
    }

    /**
     * Returns all notifications currently available concerning the given person.
     *
     * @param personId id of the person about whom notifications should be returned.
     * @return list of notifications concerning the given person.
     */
    public List<Notification> getNotificationsForPerson(long personId) {
        return createNotifications();
    }

    /**
     * Returns all notifications currently available concerning the given budget.
     *
     * @param budgetId id of the budget about which notifications should be returned.
     * @return list of notifications concerning the given budget.
     */
    public List<Notification> getNotificationsForBudget(long budgetId) {
        return createNotifications();
    }

    private List<Notification> createNotifications() {
        List<Notification> notifications = new ArrayList<Notification>();
        notifications.add(new Notification("Notification", NotificationType.MISSING_BUDGET_TOTAL, new Date()));
        notifications.add(new Notification("Notification", NotificationType.MISSING_DAILY_RATE, new Date()));
        return notifications;
    }

}
