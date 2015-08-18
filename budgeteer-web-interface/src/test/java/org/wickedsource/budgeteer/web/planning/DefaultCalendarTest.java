package org.wickedsource.budgeteer.web.planning;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class DefaultCalendarTest {

    @Test
    public void testGetDays() throws Exception {
        DefaultCalendar cal = createDefaultCalendar();

        Assert.assertEquals(365, cal.getNumberOfDays());
        Assert.assertEquals(261, cal.getNumberOfWorkingDays());
        Assert.assertEquals(104, cal.getNumberOfHolidays());

        Day firstDay = cal.getFirstDay();
        java.util.Calendar firstDayCalendar = java.util.Calendar.getInstance();
        firstDayCalendar.setTime(firstDay.getDate());
        Assert.assertEquals(2015, firstDayCalendar.get(Calendar.YEAR));
        Assert.assertEquals(Calendar.JANUARY, firstDayCalendar.get(Calendar.MONTH));
        Assert.assertEquals(1, firstDayCalendar.get(Calendar.DAY_OF_MONTH));

        Day lastDay = cal.getLastDay();
        java.util.Calendar lastDayCalendar = java.util.Calendar.getInstance();
        lastDayCalendar.setTime(lastDay.getDate());
        Assert.assertEquals(2015, lastDayCalendar.get(Calendar.YEAR));
        Assert.assertEquals(Calendar.DECEMBER, lastDayCalendar.get(Calendar.MONTH));
        Assert.assertEquals(31, lastDayCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testGetNumberOfWorkingDaysInPeriod() {
        DefaultCalendar cal = createDefaultCalendar();
        Calendar startCal = Calendar.getInstance();
        startCal.set(2015, Calendar.JULY, 9);
        Calendar endCal = Calendar.getInstance();
        endCal.set(2015, Calendar.JULY, 15);
        TimePeriod period = new TimePeriod(startCal.getTime(), endCal.getTime());

        Assert.assertEquals(5, cal.getNumberOfWorkingDaysInPeriod(period));
    }

    private DefaultCalendar createDefaultCalendar() {
        Calendar start = Calendar.getInstance();
        start.set(Calendar.YEAR, 2015);
        start.set(Calendar.MONTH, Calendar.JANUARY);
        start.set(Calendar.DAY_OF_MONTH, 1);

        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, 2015);
        end.set(Calendar.MONTH, Calendar.DECEMBER);
        end.set(Calendar.DAY_OF_MONTH, 31);

        return new DefaultCalendar(start.getTime(), end.getTime());
    }

}