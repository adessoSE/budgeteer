package org.wickedsource.budgeteer.web.components.notificationlist;

import org.wickedsource.budgeteer.service.notification.*;

import java.io.Serializable;

public class NotificationMessageFactory implements Serializable {

    private NotificationMessageAnchor anchor = new NotificationMessageAnchor();

    public String getMessageForNotification(Notification notification) {
        if (notification instanceof MissingDailyRateNotification) {
            MissingDailyRateNotification n = (MissingDailyRateNotification) notification;
            return String.format(anchor.getString("message.missingDailyRate"), n.getPersonName(), n.getStartDate(), n.getEndDate());
        } else if (notification instanceof MissingBudgetTotalNotification) {
            MissingBudgetTotalNotification n = (MissingBudgetTotalNotification) notification;
            return String.format(anchor.getString("message.missingBudgetTotal"), n.getBudgetName());
        } else if (notification instanceof EmptyWorkRecordsNotification) {
            return anchor.getString("message.emptyWorkRecords");
        } else if (notification instanceof EmptyPlanRecordsNotification) {
            return anchor.getString("message.emptyPlanRecords");
        } else if (notification instanceof MissingDailyRateForBudgetNotification) {
            MissingDailyRateForBudgetNotification n = (MissingDailyRateForBudgetNotification) notification;
            return String.format(anchor.getString("message.missingDailyRateForBudget"), n.getBudgetName(), n.getStartDate(), n.getEndDate());
        } else {
            throw new IllegalArgumentException(String.format("Notifications of type %s are not supported!", notification.getClass()));
        }
    }
}
