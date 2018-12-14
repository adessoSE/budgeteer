package de.adesso.budgeteer.service.notification;

public class MissingMailNotification extends Notification {

    private long userId;

    private String userName;

    public MissingMailNotification() {
        this.setNotificationType(NotificationType.warning);
    }

    public MissingMailNotification(long userId) {
        this.userId = userId;
        this.setNotificationType(NotificationType.warning);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
