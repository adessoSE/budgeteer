package de.adesso.budgeteer.web.pages.budgets.edit.form;

import de.adesso.budgeteer.service.notification.Notification;
import de.adesso.budgeteer.service.notification.NotificationService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class BudgetNotificationsModel extends LoadableDetachableModel<List<Notification>> {

    @SpringBean
    private NotificationService service;

    private long budgetId;

    public BudgetNotificationsModel(long budgetId) {
        Injector.get().inject(this);
        this.budgetId = budgetId;
    }

    @Override
    protected List<Notification> load() {
        return service.getNotificationsForBudget(budgetId);
    }
}
