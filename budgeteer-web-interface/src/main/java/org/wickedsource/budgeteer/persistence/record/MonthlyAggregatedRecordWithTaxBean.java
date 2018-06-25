package org.wickedsource.budgeteer.persistence.record;


import lombok.Getter;
import lombok.Setter;
import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;

import java.math.BigDecimal;

public class MonthlyAggregatedRecordWithTaxBean extends MonthlyAggregatedRecordBean
{
    @Getter
    @Setter
    private BigDecimal taxRate;

    public MonthlyAggregatedRecordWithTaxBean(int year, int month, Double hours, long valueInCents, BigDecimal taxRate)
    {
        super(year, month, hours, valueInCents);
        this.taxRate = taxRate;
    }

    public Money getValueWithTaxes()
    {
        long centAmount = getValueInCents();
        Money moneyAmount = MoneyUtil.createMoneyFromCents(centAmount);
        return MoneyUtil.getMoneyWithTaxes(moneyAmount, taxRate);
    }
}