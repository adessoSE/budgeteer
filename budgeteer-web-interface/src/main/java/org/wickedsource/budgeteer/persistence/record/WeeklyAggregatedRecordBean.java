package org.wickedsource.budgeteer.persistence.record;

public class WeeklyAggregatedRecordBean {

    private int year;

    private int week;

    private Double hours;

    private long valueInCents;

    public WeeklyAggregatedRecordBean(int year, int week, Double hours, long valueInCents) {
        this.year = year;
        this.week = week;
        this.hours = hours;
        this.valueInCents = valueInCents;
    }

    public int getYear() {
        return year;
    }

    public int getWeek() {
        return week;
    }

    public Double getHours() {
        return hours;
    }

    public long getValueInCents() {
        return valueInCents;
    }
}