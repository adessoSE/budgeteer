package org.wickedsource.budgeteer.persistence.record;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@AllArgsConstructor
public class MonthlyAggregatedRecordBean {

    private int year;

    /**
     * The month of this record (0-based).
     */
    private int month;

    private Double hours;

    private long valueInCents;

    public MonthlyAggregatedRecordBean(int year, int month, long minutes, Money dailyRate) {
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

        this.month = month;
        this.year = year;
    }
}