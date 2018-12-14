package de.adesso.budgeteer.persistence.record;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.money.Money;

import java.math.BigDecimal;

@ToString
public class MonthlyAggregatedRecordWithTaxBean extends MonthlyAggregatedRecordBean {
    @Getter
    @Setter
    private BigDecimal taxRate;

    public MonthlyAggregatedRecordWithTaxBean (int year, int month, long valueInCents, BigDecimal taxRate)
    {
        super(year, month, valueInCents);
        this.taxRate = taxRate;
    }

    public MonthlyAggregatedRecordWithTaxBean(int year, int month, Double hours, long valueInCents, BigDecimal taxRate) {
        super(year, month, hours, valueInCents);
        this.taxRate = taxRate;
    }

    public MonthlyAggregatedRecordWithTaxBean(int year, int month, long minutes, Money dailyRate, BigDecimal taxRate) {
        super(year, month, minutes, dailyRate);
        this.taxRate = taxRate;
    }

}