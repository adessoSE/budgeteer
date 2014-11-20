package org.wickedsource.budgeteer.web.components.notificationlist;

import org.wickedsource.budgeteer.service.notification.EmptyDatabaseNotification;
import org.wickedsource.budgeteer.service.notification.MissingBudgetTotalNotification;
import org.wickedsource.budgeteer.service.notification.MissingDailyRateNotification;
import org.wickedsource.budgeteer.service.notification.Notification;

import java.io.Serializable;

public class NotificationMessageFactory implements Serializable {

    private NotificationMessageAnchor anchor = new NotificationMessageAnchor();

    public String getMessageForNotification(Notification notification) {
        if (notification instanceof MissingDailyRateNotification) {
            MissingDailyRateNotification missingDailyRateNotification = (MissingDailyRateNotification) notification;
            return String.format(anchor.getString("message.missingDailyRate"), missingDailyRateNotification.getPersonName(), missingDailyRateNotification.getStartDate(), missingDailyRateNotification.getEndDate());
        } else if (notification instanceof MissingBudgetTotalNotification) {
            MissingBudgetTotalNotification missingBudgetTotalNotification = (MissingBudgetTotalNotification) notification;
            return String.format(anchor.getString("message.missingBudgetTotal"), missingBudgetTotalNotification.getBudgetName());
        } else if (notification instanceof EmptyDatabaseNotification) {
            return anchor.getString("message.emptyDatabase");
        } else {
            throw new IllegalArgumentException(String.format("Notifications of type %s are not supported!", notification.getClass()));
        }
    }
}
