package org.wickedsource.budgeteer.web.pages.budgets.edit.form;

import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.notification.Notification;
import org.wickedsource.budgeteer.service.notification.NotificationService;

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
