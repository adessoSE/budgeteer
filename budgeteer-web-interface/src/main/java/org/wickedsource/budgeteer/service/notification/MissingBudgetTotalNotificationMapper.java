package org.wickedsource.budgeteer.service.notification;

import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.persistence.budget.MissingBudgetTotalBean;
import org.wickedsource.budgeteer.service.AbstractMapper;

@Component
public class MissingBudgetTotalNotificationMapper
		extends AbstractMapper<MissingBudgetTotalBean, Notification> {

	@Override
	public Notification map(MissingBudgetTotalBean missingTotal) {
		MissingBudgetTotalNotification notification = new MissingBudgetTotalNotification();
		notification.setBudgetId(missingTotal.getBudgetId());
		notification.setBudgetName(missingTotal.getBudgetName());
		return notification;
	}
}
