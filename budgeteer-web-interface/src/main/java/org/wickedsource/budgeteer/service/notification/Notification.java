package org.wickedsource.budgeteer.service.notification;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class Notification implements Serializable {
    private NotificationType notificationType = NotificationType.warning;

    public enum NotificationType {
        warning, info
    }
}
