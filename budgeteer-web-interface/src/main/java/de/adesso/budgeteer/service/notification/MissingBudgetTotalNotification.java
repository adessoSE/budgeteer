package de.adesso.budgeteer.service.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MissingBudgetTotalNotification extends Notification {

    private long budgetId;
    private String budgetName;
}
