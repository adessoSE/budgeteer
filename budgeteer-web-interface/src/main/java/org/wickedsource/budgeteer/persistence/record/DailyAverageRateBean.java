package org.wickedsource.budgeteer.persistence.record;

import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;

public class DailyAverageRateBean {

    private int year;

    private int month;

    private int day;

    private double rate;

    public DailyAverageRateBean(int year, int month, int day, double rate) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.rate = rate;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public double getRateInCents() {
        return rate;
    }

    public Money getRate(){
        return MoneyUtil.createMoneyFromCents((long) Math.floor(rate));
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
