package org.wickedsource.budgeteer.service.notification;

import de.adesso.budgeteer.persistence.record.MissingDailyRateForBudgetBean;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.service.AbstractMapper;

@Component
public class MissingDailyRateForBudgetNotificationMapper
    extends AbstractMapper<MissingDailyRateForBudgetBean, Notification> {

  @Override
  public Notification map(MissingDailyRateForBudgetBean rate) {
    MissingDailyRateForBudgetNotification notification =
        new MissingDailyRateForBudgetNotification();
    notification.setStartDate(rate.getStartDate());
    notification.setEndDate(rate.getEndDate());
    notification.setPersonId(rate.getPersonId());
    notification.setPersonName(rate.getPersonName());
    notification.setBudgetName(rate.getBudgetName());
    return notification;
  }
}
