package org.wickedsource.budgeteer.service.record;

import java.util.Date;

import lombok.Data;

import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;

@Data
public class AggregatedRecord {

	private String aggregationPeriodTitle;
	private Date aggregationPeriodStart;
	private Date aggregationPeriodEnd;
	private Double hours;
	private Money budgetPlanned;
	private Money budgetBurned;

	public Money getDifference() {
		Money first = budgetPlanned != null ? budgetPlanned : MoneyUtil.createMoney(0);
		Money second = budgetBurned != null ? budgetBurned : MoneyUtil.createMoney(0);
		return first.minus(second);
	}
}
