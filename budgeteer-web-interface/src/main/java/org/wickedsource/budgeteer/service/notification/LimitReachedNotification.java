package org.wickedsource.budgeteer.service.notification;

public class LimitReachedNotification extends Notification {

    private long budgetId;

    private String budgetName;

    public LimitReachedNotification() {
        this.setNotificationType(NotificationType.info);
    }

    public long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(long budgetId) {
        this.budgetId = budgetId;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }
}
