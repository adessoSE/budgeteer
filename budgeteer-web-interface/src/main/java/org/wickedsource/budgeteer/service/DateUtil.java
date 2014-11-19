package org.wickedsource.budgeteer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class DateUtil {

    @Autowired
    private DateProvider dateProvider;

    /**
     * Returns the current date minus a number of weeks.
     */
    public Date weeksAgo(int numberOfWeeks) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateProvider.currentDate());
        c.add(Calendar.WEEK_OF_YEAR, -numberOfWeeks + 1); // +1, because we want to have the current week included
        return c.getTime();
    }

    /**
     * Returns the current date minus a number of months.
     */
    public Date monthsAgo(int numberOfMonths) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateProvider.currentDate());
        c.add(Calendar.MONTH, -numberOfMonths + 1); // +1, because we want to have the current month included
        return c.getTime();
    }

    /**
     * Returns the current date minus a number of days.
     */
    public Date daysAgo(int numberOfDays) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateProvider.currentDate());
        c.add(Calendar.DAY_OF_YEAR, -numberOfDays + 1); // +1, because we want to have the current day included
        return c.getTime();
    }

}
