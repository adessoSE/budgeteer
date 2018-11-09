package org.wickedsource.budgeteer.web.pages.budgets.manualRecords.add.form;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.notification.Notification;
import org.wickedsource.budgeteer.service.notification.NotificationService;

import java.util.List;

public class ManualRecordNotificationsModel extends LoadableDetachableModel<List<Notification>> {

    @SpringBean
    private NotificationService service;

    private long budgetId;

    public ManualRecordNotificationsModel(long budgetId) {
        Injector.get().inject(this);
        this.budgetId = budgetId;
    }

    @Override
    protected List<Notification> load() {
        return service.getNotificationsForBudget(budgetId);
    }
}
