package org.wickedsource.budgeteer.service.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class MissingContractForBudgetNotification extends Notification{
	private long budgetId;
}
