package de.adesso.budgeteer.service.notification;

import de.adesso.budgeteer.persistence.record.MissingDailyRateBean;
import de.adesso.budgeteer.service.AbstractMapper;
import org.springframework.stereotype.Component;

@Component
public class MissingDailyRateNotificationMapper extends AbstractMapper<MissingDailyRateBean, Notification> {

    @Override
    public Notification map(MissingDailyRateBean rate) {
        MissingDailyRateNotification notification = new MissingDailyRateNotification();
        notification.setStartDate(rate.getStartDate());
        notification.setEndDate(rate.getEndDate());
        notification.setPersonId(rate.getPersonId());
        notification.setPersonName(rate.getPersonName());
        return notification;
    }

}
