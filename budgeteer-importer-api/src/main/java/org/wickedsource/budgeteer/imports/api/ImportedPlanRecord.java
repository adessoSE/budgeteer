package org.wickedsource.budgeteer.imports.api;

import org.joda.money.Money;

public class ImportedPlanRecord extends ImportedRecord {

	private int minutesPlanned;

	private Money dailyRate;

	public int getMinutesPlanned() {
		return minutesPlanned;
	}

	public void setMinutesPlanned(int minutesPlanned) {
		this.minutesPlanned = minutesPlanned;
	}

	public Money getDailyRate() {
		return dailyRate;
	}

	public void setDailyRate(Money dailyRate) {
		this.dailyRate = dailyRate;
	}
}
