package org.wickedsource.budgeteer.service.notification;

import de.adesso.budgeteer.persistence.budget.LimitReachedBean;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.service.AbstractMapper;

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
