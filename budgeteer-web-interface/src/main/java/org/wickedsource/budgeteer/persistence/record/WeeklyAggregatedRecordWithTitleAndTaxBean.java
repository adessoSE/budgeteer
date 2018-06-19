package org.wickedsource.budgeteer.persistence.record;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class WeeklyAggregatedRecordWithTitleAndTaxBean extends WeeklyAggregatedRecordWithTaxBean {
    @Getter
    @Setter
    private String title;

    public WeeklyAggregatedRecordWithTitleAndTaxBean(int year, int week, Double hours, long valueInCents, BigDecimal taxRate, String title) {
        super(year, week, hours, valueInCents, taxRate);
        this.title = title;
    }
}