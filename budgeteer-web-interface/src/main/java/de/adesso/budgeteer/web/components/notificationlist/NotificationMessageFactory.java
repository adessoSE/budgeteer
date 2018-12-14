package de.adesso.budgeteer.web.components.notificationlist;

import de.adesso.budgeteer.service.notification.*;
import de.adesso.budgeteer.web.PropertyLoader;
import de.adesso.budgeteer.service.notification.*;

import java.io.Serializable;

public class NotificationMessageFactory implements Serializable {

    public String getNotificationTypeForNotification(Notification notification) {
        return notification.getNotificationType().toString();
    }

    public String getMessageForNotification(Notification notification) {
        if (notification instanceof MissingDailyRateNotification) {
            MissingDailyRateNotification n = (MissingDailyRateNotification) notification;
            return String.format(PropertyLoader.getProperty(NotificationMessageAnchor.class, "message.missingDailyRate"), n.getPersonName(), n.getStartDate(), n.getEndDate());
        } else if (notification instanceof MissingBudgetTotalNotification) {
            MissingBudgetTotalNotification n = (MissingBudgetTotalNotification) notification;
            return String.format(PropertyLoader.getProperty(NotificationMessageAnchor.class, "message.missingBudgetTotal"), n.getBudgetName());
        } else if (notification instanceof EmptyWorkRecordsNotification) {
            return PropertyLoader.getProperty(NotificationMessageAnchor.class, "message.emptyWorkRecords");
        } else if (notification instanceof EmptyPlanRecordsNotification) {
            return PropertyLoader.getProperty(NotificationMessageAnchor.class, "message.emptyPlanRecords");
        } else if (notification instanceof MissingDailyRateForBudgetNotification) {
            MissingDailyRateForBudgetNotification n = (MissingDailyRateForBudgetNotification) notification;
            return String.format(PropertyLoader.getProperty(NotificationMessageAnchor.class, "message.missingDailyRateForBudget"), n.getBudgetName(), n.getStartDate(), n.getEndDate());
        } else if (notification instanceof MissingContractForBudgetNotification) {
            return String.format(PropertyLoader.getProperty(NotificationMessageAnchor.class, "message.missingContract"));
        } else if (notification instanceof LimitReachedNotification) {
            LimitReachedNotification n = (LimitReachedNotification) notification;
            return String.format(PropertyLoader.getProperty(NotificationMessageAnchor.class, "message.limitReached"), n.getBudgetName());
        } else if (notification instanceof MailNotVerifiedNotification) {
            MailNotVerifiedNotification n = (MailNotVerifiedNotification) notification;
            return String.format(PropertyLoader.getProperty(NotificationMessageAnchor.class, "message.mailNotVerified"), n.getUserMail());
        } else if (notification instanceof MissingMailNotification) {
            MissingMailNotification n = (MissingMailNotification) notification;
            return String.format(PropertyLoader.getProperty(NotificationMessageAnchor.class, "message.missingMail"));
        } else {
            throw new IllegalArgumentException(String.format("Notifications of type %s are not supported!", notification.getClass()));
        }
    }
}
