package org.wickedsource.budgeteer.service.statistics;

import lombok.Data;
import org.joda.money.Money;
import org.wickedsource.budgeteer.web.BudgeteerSession;

import java.util.ArrayList;
import java.util.List;

@Data
public class MoneySeries {

    private String name;
    private List<Money> valuesNet = new ArrayList<>();
    private List<Money> valuesGross = new ArrayList<>();

    public void add(Money value) {
        valuesNet.add(value);
    }

    /***
     * @return Returns the size of the MoneySeries
     */
    public int getSize() {
        return valuesNet.size();
    }

    /**
     * @return Returns the valuesNet with or without taxes, according to the current state of the session
     */
    public List<Money> getMoneyValues() {
        if (BudgeteerSession.get().isTaxEnabled()) {
            return valuesGross;
        } else {
            return valuesNet;
        }
    }
}
