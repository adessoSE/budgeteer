package org.wickedsource.budgeteer.persistence.record;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;

import java.math.BigDecimal;

public class WeeklyAggregatedRecordWithTaxBean extends WeeklyAggregatedRecordBean {

    @Getter
    @Setter
    private BigDecimal taxRate;

    public WeeklyAggregatedRecordWithTaxBean(int year, int week, Double hours, long valueInCents, BigDecimal taxRate) {
        super(year, week, hours, valueInCents);
        this.taxRate = taxRate;
    }

    public WeeklyAggregatedRecordWithTaxBean(int year, int month, int week, long minutes, Money dailyRate, BigDecimal taxRate) {
        super(year, month, week, minutes, dailyRate);
        this.taxRate = taxRate;
    }
}
