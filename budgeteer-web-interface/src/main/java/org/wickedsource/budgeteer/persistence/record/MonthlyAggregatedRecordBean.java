package org.wickedsource.budgeteer.persistence.record;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyAggregatedRecordBean {

	private int year;

	/**
	* The month of this record (0-based).
	*/
	private int month;

	private Double hours;

	private long valueInCents;
}