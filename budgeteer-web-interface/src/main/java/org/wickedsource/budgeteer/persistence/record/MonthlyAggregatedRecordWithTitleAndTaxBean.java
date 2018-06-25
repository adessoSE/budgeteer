package org.wickedsource.budgeteer.persistence.record;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class MonthlyAggregatedRecordWithTitleAndTaxBean extends MonthlyAggregatedRecordWithTaxBean {

	@Getter
	@Setter
	private String title;

	public MonthlyAggregatedRecordWithTitleAndTaxBean(int year, int month, Double hours, long valueInCents, String title, BigDecimal taxRate)
	{
		super(year, month, hours, valueInCents, taxRate);
		this.title = title;
	}
}
