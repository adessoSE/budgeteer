package org.wickedsource.budgeteer.persistence.record;

import lombok.Getter;
import lombok.Setter;

public class MonthlyAggregatedRecordWithTitleBean extends MonthlyAggregatedRecordBean {
	@Getter @Setter
	private String title;

	public MonthlyAggregatedRecordWithTitleBean(int year, int month, Double hours, long valueInCents, String title) {
		super(year, month, hours, valueInCents);
		this.title = title;
	}
}
