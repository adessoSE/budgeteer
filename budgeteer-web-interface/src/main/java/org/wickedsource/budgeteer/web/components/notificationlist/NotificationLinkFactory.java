package org.wickedsource.budgeteer.web.components.notificationlist;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.service.notification.MissingBudgetTotalNotification;
import org.wickedsource.budgeteer.service.notification.MissingDailyRateNotification;
import org.wickedsource.budgeteer.service.notification.Notification;
import org.wickedsource.budgeteer.web.pages.budgets.edit.EditBudgetPage;
import org.wickedsource.budgeteer.web.pages.person.edit.EditPersonPage;

import java.io.Serializable;

public class NotificationLinkFactory implements Serializable {

    public WebPage getLinkForNotification(Notification notification, Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        if (notification instanceof MissingDailyRateNotification) {
            MissingDailyRateNotification missingDailyRateNotification = (MissingDailyRateNotification) notification;
            return new EditPersonPage(EditPersonPage.createParameters(missingDailyRateNotification.getPersonId()), backlinkPage, backlinkParameters);
        } else if (notification instanceof MissingBudgetTotalNotification) {
            MissingBudgetTotalNotification missingBudgetTotalNotification = (MissingBudgetTotalNotification) notification;
            return new EditBudgetPage(EditBudgetPage.createParameters(missingBudgetTotalNotification.getBudgetId()), backlinkPage, backlinkParameters);
        } else {
            throw new IllegalArgumentException(String.format("Notifications of type %s are not supported!", notification.getClass()));
        }
    }
}
