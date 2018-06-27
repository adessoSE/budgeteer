package org.wickedsource.budgeteer.persistence.record;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class WeeklyAggregatedRecordWithTitleAndTaxBean extends WeeklyAggregatedRecordWithTaxBean {
	@Getter
	@Setter
	private String title;

	public WeeklyAggregatedRecordWithTitleAndTaxBean(int year, int week, Double hours, long valueInCents, BigDecimal taxRate, String title) {
		super(year, week, hours, valueInCents, taxRate);
		this.title = title;
	}
}