package org.wickedsource.budgeteer.web.pages.budgets.overview.report.form;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.notification.Notification;
import org.wickedsource.budgeteer.service.notification.NotificationService;

import java.util.List;

public class BudgetReportNotificationModel extends LoadableDetachableModel<List<Notification>>  {

    @SpringBean
    private NotificationService service;

    public BudgetReportNotificationModel() {
        Injector.get().inject(this);
    }

    @Override
    protected List<Notification> load() {
        //return service.getNotificationsForBudget(budgetId);
    	return null;
    }

}
