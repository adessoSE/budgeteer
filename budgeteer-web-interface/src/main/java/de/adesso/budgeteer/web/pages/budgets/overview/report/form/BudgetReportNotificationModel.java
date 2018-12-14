package de.adesso.budgeteer.web.pages.budgets.overview.report.form;

import de.adesso.budgeteer.service.notification.Notification;
import de.adesso.budgeteer.service.notification.NotificationService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

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
