package org.wickedsource.budgeteer.service.notification;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class LimitReachedNotification extends Notification {

    private long budgetId;
    private String budgetName;

    public LimitReachedNotification() {
        this.setNotificationType(NotificationType.info);
    }
}
