package org.wickedsource.budgeteer.web.components.notificationlist;

import org.wickedsource.budgeteer.service.notification.MissingBudgetTotalNotification;
import org.wickedsource.budgeteer.service.notification.MissingDailyRateNotification;
import org.wickedsource.budgeteer.service.notification.Notification;

public class NotificationMessageFactory {

    private NotificationMessageAnchor anchor = new NotificationMessageAnchor();

    public String getMessageForNotification(Notification notification) {
        if (notification instanceof MissingDailyRateNotification) {
            return anchor.getString("message.missingDailyRate");
        } else if (notification instanceof MissingBudgetTotalNotification) {
            return anchor.getString("message.missingBudgetTotal");
        } else {
            throw new IllegalArgumentException(String.format("Notifications of type %s are not supported!", notification.getClass()));
        }
    }
}
