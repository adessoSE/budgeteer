package org.wickedsource.budgeteer.persistence.record;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.money.Money;

import java.math.BigDecimal;

@ToString
public class MonthlyAggregatedRecordWithTitleAndTaxBean extends MonthlyAggregatedRecordWithTaxBean {

    @Getter
    @Setter
    private String title;

    public MonthlyAggregatedRecordWithTitleAndTaxBean(int year, int month, Double hours, long valueInCents, String title, BigDecimal taxRate)
    {
        super(year, month, hours, valueInCents, taxRate);
        this.title = title;
    }

    public MonthlyAggregatedRecordWithTitleAndTaxBean(int year, int month, long valueInCents, String title, BigDecimal taxRate)
    {
        super(year, month, valueInCents, taxRate);
        this.title = title;
    }

    public MonthlyAggregatedRecordWithTitleAndTaxBean(int year, int month, long minutes, Money dailyRate, String title, java.math.BigDecimal taxRate)
    {
        super(year, month, minutes, dailyRate, taxRate);
        this.title = title;
    }
}
