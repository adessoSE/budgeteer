package org.wickedsource.budgeteer.service.record;

import lombok.Data;
import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;

import java.util.Date;

@Data
public class AggregatedRecord {
    private String aggregationPeriodTitle;
    private Date aggregationPeriodStart;
    private Date aggregationPeriodEnd;
    private Double hours;
    private Money budgetPlannedNet;
    private Money budgetBurnedNet;
    private Money budgetPlannedGross;
    private Money budgetBurnedGross;

    public Money getDifference() {
        Money first = budgetPlannedNet != null ? budgetPlannedNet : MoneyUtil.createMoney(0);
        Money second = budgetBurnedNet != null ? budgetBurnedNet : MoneyUtil.createMoney(0);
        return first.minus(second);
    }

    public Money getDifferenceGross(){
        Money first = budgetPlannedGross != null ? budgetPlannedGross : MoneyUtil.createMoney(0);
        Money second = budgetBurnedGross != null ? budgetBurnedGross : MoneyUtil.createMoney(0);
        return first.minus(second);
    }
}
