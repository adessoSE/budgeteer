package org.wickedsource.budgeteer.service.notification;

public class MailNotVerifiedNotification extends Notification {

    private long userId;

    private String userMail;

    public MailNotVerifiedNotification() {
        this.setNotificationType(NotificationType.warning);
    }

    public MailNotVerifiedNotification(long userId, String userMail) {
        this.userId = userId;
        this.userMail = userMail;
        this.setNotificationType(NotificationType.warning);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }
}
