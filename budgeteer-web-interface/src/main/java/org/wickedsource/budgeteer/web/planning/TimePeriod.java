package org.wickedsource.budgeteer.web.planning;

import java.util.*;
import java.util.Calendar;

public class TimePeriod {

    private final Date start;

    private final Date end;

    public TimePeriod(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public java.util.Calendar getStartCalendar(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        return cal;
    }

    public java.util.Calendar getEndCalendar(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(end);
        return cal;
    }

    public Date getEnd() {
        return end;
    }
}
