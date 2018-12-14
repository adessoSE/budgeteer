package de.adesso.budgeteer.web.pages.contract.details.contractDetailChart;

import lombok.Data;
import org.joda.money.Money;

import java.util.LinkedList;
import java.util.List;

@Data
public class ContractDetailBudgetChart {
    private List<Money> remainingTotalBudget = new LinkedList<>();
    private List<Money> burnedMoneyAllBudgets = new LinkedList<>();
    private List<Money> burnedMoneyInvoice = new LinkedList<>();
}
