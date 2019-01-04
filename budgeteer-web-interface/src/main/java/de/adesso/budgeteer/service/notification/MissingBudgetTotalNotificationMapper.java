package de.adesso.budgeteer.service.notification;

import de.adesso.budgeteer.persistence.budget.MissingBudgetTotalBean;
import de.adesso.budgeteer.service.AbstractMapper;
import org.springframework.stereotype.Component;

@Component
public class MissingBudgetTotalNotificationMapper extends AbstractMapper<MissingBudgetTotalBean, Notification> {

    @Override
    public Notification map(MissingBudgetTotalBean missingTotal) {
        MissingBudgetTotalNotification notification = new MissingBudgetTotalNotification();
        notification.setBudgetId(missingTotal.getBudgetId());
        notification.setBudgetName(missingTotal.getBudgetName());
        return notification;
    }

}
