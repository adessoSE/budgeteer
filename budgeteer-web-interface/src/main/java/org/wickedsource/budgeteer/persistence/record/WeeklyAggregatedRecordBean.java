package org.wickedsource.budgeteer.persistence.record;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeeklyAggregatedRecordBean {

	private int year;

	private int week;

	private Double hours;

	private long valueInCents;

}