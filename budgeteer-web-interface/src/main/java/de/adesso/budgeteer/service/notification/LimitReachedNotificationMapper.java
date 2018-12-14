package de.adesso.budgeteer.service.notification;

import de.adesso.budgeteer.persistence.budget.LimitReachedBean;
import org.springframework.stereotype.Component;
import de.adesso.budgeteer.service.AbstractMapper;

@Component
public class LimitReachedNotificationMapper extends AbstractMapper<LimitReachedBean, Notification> {

    @Override
    public Notification map(LimitReachedBean sourceObject) {
        LimitReachedNotification notification = new LimitReachedNotification();
        notification.setBudgetId(sourceObject.getBudgetId());
        notification.setBudgetName(sourceObject.getBudgetName());
        return notification;
    }
}
