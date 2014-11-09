package org.wickedsource.budgeteer.persistence.record;

public class MonthlyAggregatedRecordBean {

    private int year;

    private int month;

    private Double hours;

    private long valueInCents;

    public MonthlyAggregatedRecordBean(int year, int month, Double hours, long valueInCents) {
        this.year = year;
        this.month = month;
        this.hours = hours;
        this.valueInCents = valueInCents;
    }

    public int getYear() {
        return year;
    }

    /**
     * The month of this record (0-based).
     */
    public int getMonth() {
        return month;
    }

    public Double getHours() {
        return hours;
    }

    public long getValueInCents() {
        return valueInCents;
    }
}