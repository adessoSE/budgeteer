package org.wickedsource.budgeteer.persistence.record;

import lombok.Getter;
import lombok.Setter;

public class MonthlyAggregatedRecordWithTitleBean extends MonthlyAggregatedRecordBean {
	@Getter @Setter
	private String title;

	public MonthlyAggregatedRecordWithTitleBean(int year, int week, Double hours, long valueInCents, String title) {
		super(year, week, hours, valueInCents);
		this.title = title;
	}
}
