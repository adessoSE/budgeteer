package org.wickedsource.budgeteer.persistence.record;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@AllArgsConstructor
public class WeeklyAggregatedRecordBean {

    private int year;

    private int week;

    private Double hours;

    private long valueInCents;

    private int month;

    public WeeklyAggregatedRecordBean(int year, int week, Double hours, long valueInCents) {
        this.week = week;
        this.year = year;
        this.hours = hours;
        this.valueInCents = valueInCents;
    }

    public WeeklyAggregatedRecordBean(int year, int week, long valueInCents) {
        this.week = week;
        this.year = year;
        this.valueInCents = valueInCents;
        hours = 0.0;
    }

    public WeeklyAggregatedRecordBean(int year, int month, int week, long valueInCents) {
        this.week = week;
        this.month = month;
        this.year = year;
        this.valueInCents = valueInCents;
        hours = 0.0;
    }

    public WeeklyAggregatedRecordBean(int year, int month, int week, long minutes, Money dailyRate) {
        this.hours = ((double) minutes) / 60.0;

        // Round the hours to full half hours
        BigDecimal fullHours = new BigDecimal(hours);
        BigDecimal beforeCommaPart = fullHours.setScale(0, RoundingMode.DOWN);
        BigDecimal afterComaPart = fullHours.subtract(beforeCommaPart);

        if (afterComaPart.compareTo((new BigDecimal(0.25))) < 0) {
            this.hours = beforeCommaPart.doubleValue();
        } else if (afterComaPart.compareTo((new BigDecimal(0.75))) < 0) {
            this.hours = beforeCommaPart.add(new BigDecimal(0.5)).doubleValue();
        } else {
            this.hours = beforeCommaPart.add(new BigDecimal(1)).doubleValue();
        }

        // Multiply the hours with the daily rate
        Double moneySum = MoneyUtil.toDouble(dailyRate.multipliedBy(this.hours / 8.0, RoundingMode.HALF_UP));
        valueInCents = (long) (moneySum * 100);

        this.week = week;
        this.month = month;
        this.year = year;
    }
}