package org.wickedsource.budgeteer.service.record;

import lombok.Data;
import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;

import java.util.*;

@Data
public class AggregatedRecord {
    private String aggregationPeriodTitle;
    private Date aggregationPeriodStart;
    private Date aggregationPeriodEnd;
    private Double hours;
    private Money budgetPlanned_net;
    private Money budgetBurned_net;
    private Money budgetPlanned_gross;
    private Money budgetBurned_gross;

    public Money getDifference() {
        Money first = budgetPlanned_net != null ? budgetPlanned_net : MoneyUtil.createMoney(0);
        Money second = budgetBurned_net != null ? budgetBurned_net : MoneyUtil.createMoney(0);
        return first.minus(second);
    }

    public Money getDifference_gross(){
        Money first = budgetPlanned_gross != null ? budgetPlanned_gross : MoneyUtil.createMoney(0);
        Money second = budgetBurned_gross != null ? budgetBurned_gross : MoneyUtil.createMoney(0);
        return first.minus(second);
    }
}
