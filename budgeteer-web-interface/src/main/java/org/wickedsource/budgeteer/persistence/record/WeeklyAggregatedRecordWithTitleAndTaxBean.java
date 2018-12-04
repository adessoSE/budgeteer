package org.wickedsource.budgeteer.persistence.record;

import lombok.Getter;
import lombok.Setter;
import org.joda.money.Money;

import java.math.BigDecimal;

public class WeeklyAggregatedRecordWithTitleAndTaxBean extends WeeklyAggregatedRecordWithTaxBean {
    @Getter
    @Setter
    private String title;

    public WeeklyAggregatedRecordWithTitleAndTaxBean(int year, int month, int week, long valueInCents, BigDecimal taxRate, String title) {
        super(year, month, week, valueInCents, taxRate);
        this.title = title;
    }

    public WeeklyAggregatedRecordWithTitleAndTaxBean(int year, int month, int week, long minutes, Money dailyRate, BigDecimal taxRate, String title) {
        super(year, month, week, minutes, dailyRate, taxRate);
        this.title = title;
    }
}