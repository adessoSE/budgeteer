package org.wickedsource.budgeteer.service.statistics;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import org.joda.money.Money;
import org.wickedsource.budgeteer.web.BudgeteerSession;

@Data
public class MoneySeries {

	private String name;
	private List<Money> values = new ArrayList<Money>();
	private List<Money> values_gross = new ArrayList<Money>();

	public void add(Money value) {
		values.add(value);
	}

	/**
	* @return Returns the values with or without taxes, according to the current state of the session
	* */
	public List<Money> getMoneyValues()
	{
		if(BudgeteerSession.get().isTaxEnabled())
		{
			return values_gross;
		}
		else {
			return values;
		}
	}

}
