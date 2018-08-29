package org.wickedsource.budgeteer.service.notification;

import java.io.Serializable;

public abstract class Notification implements Serializable {
    private NotificationType notificationType = NotificationType.warning;

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public enum NotificationType {
        warning, info
    }
}
