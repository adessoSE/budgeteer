package org.wickedsource.budgeteer.service.notification;

import java.io.Serializable;
import java.util.Date;

public class Notification implements Serializable {

    private Date date;

    private String message;

    private NotificationType type;

    public Notification(String message, NotificationType type, Date date) {
        this.message = message;
        this.type = type;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public NotificationType getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

}
