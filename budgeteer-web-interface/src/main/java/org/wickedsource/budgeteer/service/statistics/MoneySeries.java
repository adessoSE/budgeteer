package org.wickedsource.budgeteer.service.statistics;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import org.joda.money.Money;

@Data
public class MoneySeries {

	private String name;
	private List<Money> values = new ArrayList<Money>();

	public void add(Money value) {
		values.add(value);
	}

}
