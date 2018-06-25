package org.wickedsource.budgeteer.persistence.record;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;

public class WeeklyAggregatedRecordWithTaxBean extends WeeklyAggregatedRecordBean {

	@Getter
	@Setter
	private BigDecimal taxRate;

	public WeeklyAggregatedRecordWithTaxBean(int year, int week, Double hours, long valueInCents, BigDecimal taxRate) {
		super(year, week, hours, valueInCents);
		this.taxRate = taxRate;
	}

	/**
	* Calculate the gross values by adding taxes according to the tax rate.
	* @return the valuesInCents as gross value.
	*/
	public Money getValueWithTaxes()
	{
		long centAmount = this.getValueInCents();
		Money moneyAmount = MoneyUtil.createMoneyFromCents(centAmount);
		return MoneyUtil.getMoneyWithTaxes(moneyAmount, taxRate);
	}
}
