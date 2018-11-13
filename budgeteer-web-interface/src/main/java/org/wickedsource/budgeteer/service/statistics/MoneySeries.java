package org.wickedsource.budgeteer.service.statistics;

import lombok.Data;
import org.joda.money.Money;
import org.wickedsource.budgeteer.web.BudgeteerSession;

import java.util.ArrayList;
import java.util.List;

@Data
public class MoneySeries {

    private String name;
    private List<Money> values = new ArrayList<>();
    private List<Money> values_gross = new ArrayList<>();

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
