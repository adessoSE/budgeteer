package org.wickedsource.budgeteer.service.notification;

import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.persistence.record.MissingDailyRate;
import org.wickedsource.budgeteer.service.AbstractMapper;

@Component
public class MissingDailyRateNotificationMapper extends AbstractMapper<MissingDailyRate, Notification> {

    @Override
    public Notification map(MissingDailyRate rate) {
        MissingDailyRateNotification notification = new MissingDailyRateNotification();
        notification.setStartDate(rate.getStartDate());
        notification.setEndDate(rate.getEndDate());
        notification.setPersonId(rate.getPersonId());
        notification.setPersonName(rate.getPersonName());
        return notification;
    }

}
