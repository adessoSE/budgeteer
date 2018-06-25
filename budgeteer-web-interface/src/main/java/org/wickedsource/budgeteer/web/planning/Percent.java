package org.wickedsource.budgeteer.web.planning;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.joda.money.Money;

public class Percent {

	public static final Percent ZERO = new Percent(0);

	private final int percentage;

	public Percent(int percentage) {
		this.percentage = percentage;
	}

	public int getPercentage() {
		return percentage;
	}

	public Percent add(Percent percent){
		return new Percent(percent.getPercentage() + this.percentage);
	}

	public boolean greaterThan(Percent percent){
		return this.percentage > percent.percentage;
	}

	public BigDecimal of(BigDecimal value){
		BigDecimal result = value.multiply(BigDecimal.valueOf(percentage));
		result = result.divide(BigDecimal.valueOf(100));
		return result;
	}

	public Money of(Money value){
		Money result = value.multipliedBy(percentage);
		result = result.dividedBy(BigDecimal.valueOf(100), RoundingMode.HALF_EVEN);
		return result;
	}
}
