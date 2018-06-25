package org.wickedsource.budgeteer.web.pages.contract.details.contractDetailChart;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;

import org.joda.money.Money;

@Data
public class ContractDetailBudgetChart {
	private List<Money> remainingTotalBudget = new LinkedList<Money>();
	private List<Money> burnedMoneyAllBudgets = new LinkedList<Money>();
	private List<Money> burnedMoneyInvoice = new LinkedList<Money>();
}
