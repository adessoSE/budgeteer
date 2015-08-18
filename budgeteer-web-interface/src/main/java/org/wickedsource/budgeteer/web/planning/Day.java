package org.wickedsource.budgeteer.web.planning;

import java.util.Date;

public class Day {

    private final Date date;

    private final boolean holiday;

    private Day nextDay;

    public Day(Date date, boolean holiday) {
        this.date = date;
        this.holiday = holiday;
    }

    public Day nextDay(){
        return this.nextDay;
    }

    protected void setNextDay(Day nextDay) {
        this.nextDay = nextDay;
    }

    public Date getDate(){
        return this.date;
    }

    public boolean isHoliday(){
        return this.holiday;
    }
}
