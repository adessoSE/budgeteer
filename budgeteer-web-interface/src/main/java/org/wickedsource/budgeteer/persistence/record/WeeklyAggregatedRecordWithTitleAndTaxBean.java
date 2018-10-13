package org.wickedsource.budgeteer.persistence.record;

import lombok.Getter;
import lombok.Setter;
import org.joda.money.Money;

public class WeeklyAggregatedRecordWithTitleAndTaxBean extends WeeklyAggregatedRecordWithTaxBean {
    @Getter
    @Setter
    private String title;

    public WeeklyAggregatedRecordWithTitleAndTaxBean(int year, int month, int week, long minutes, Money dailyRate, java.math.BigDecimal taxRate, java.lang.String title) {
        super(year, month, week, minutes, dailyRate, taxRate);
        this.title = title;
    }
}