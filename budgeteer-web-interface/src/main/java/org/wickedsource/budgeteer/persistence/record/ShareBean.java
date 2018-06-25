package org.wickedsource.budgeteer.persistence.record;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;

@Data
@AllArgsConstructor
public class ShareBean {

	private String name;

	private long valueInCents;

	public Money getValue(){
		return MoneyUtil.createMoneyFromCents(valueInCents);
	}
}
