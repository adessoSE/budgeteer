package org.wickedsource.budgeteer.persistence.record;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;

@AllArgsConstructor
@Data
public class DailyAverageRateBean {

	private int year;
	private int month;
	private int day;
	private double rate;

	public double getRateInCents() {
		return rate;
	}

	public Money getRate() {
		return MoneyUtil.createMoneyFromCents((long) Math.floor(rate));
	}
}
