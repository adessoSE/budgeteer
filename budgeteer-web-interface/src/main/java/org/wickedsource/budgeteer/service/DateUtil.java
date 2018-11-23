package org.wickedsource.budgeteer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

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

    /**
     * Returns whether the date is between the start and end of the dateRange
     *
     * @param d         date to be checked
     * @return true if the date d is in the given dateRange
     */
    public static boolean isDateInDateRange(Date d, DateRange dateRange) {
        return d.compareTo(dateRange.getStartDate()) >= 0 && d.compareTo(dateRange.getEndDate()) <= 0;
    }

    /**
     * Checks whether the two DateRanges are overlapping (ore one contains the other)
     */
    public static boolean isDateRangeOverlapping(DateRange d1, DateRange d2) {
        return ((isDateInDateRange(d1.getStartDate(), d2) || isDateInDateRange(d1.getEndDate(), d2)) ||
                (isDateInDateRange(d2.getStartDate(), d1) && isDateInDateRange(d2.getEndDate(), d1)));
    }

    public static Date getBeginOfYear() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 0);
        return cal.getTime();
    }

    public static Date getEndOfYear() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.set(Calendar.MONTH, 11);
        return cal.getTime();
    }

    /**
     * Returns a list of the current year +- range
     */
    public static List<Integer> getCurrentYears(int range) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        int currentYear = cal.get(Calendar.YEAR);
        List<Integer> result = new LinkedList<>();
        for (int i = 0; i < range * 2; i++) {
            result.add(currentYear - range + i);
        }
        return result;
    }

    public static Date getEndOfThisMonth() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * Checks, if a date specified by a month and a year is before a given Date
     *
     * @param month month of the date
     * @param year  year of the date
     * @param date  Date to compare with
     */
    public static boolean isBefore(int month, int year, Date date) {
        Calendar cal = getCalenderFromMonthAndYear(month, year);

        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(date);

        return cal.before(cal2);
    }

    /**
     * Checks, if a date specified by a month and a year is after a given Date
     *
     * @param month month of the date
     * @param year  year of the date
     * @param date  Date to compare with
     */
    public static boolean isAfter(int month, int year, Date date) {
        Calendar cal = getCalenderFromMonthAndYear(month, year);

        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(date);

        return cal.after(cal2);
    }

    /**
     * Checks, if a date is before a second date. Both dates are specified by a month and a year.
     *
     * @param month1 month of first date
     * @param year1  year of first date
     * @param month2 month of second date
     * @param year2  year of second date
     */
    public static boolean isBefore(int month1, int year1, int month2, int year2) {
        Calendar cal = getCalenderFromMonthAndYear(month1, year1);
        Calendar cal2 = getCalenderFromMonthAndYear(month2, year2);
        return cal.before(cal2);
    }

    /**
     * Checks, if a date is after a second date. Both dates are specified by a month and a year.
     *
     * @param month1 month of first date
     * @param year1  year of first date
     * @param month2 month of second date
     * @param year2  year of second date
     */
    public static boolean isAfter(int month1, int year1, int month2, int year2) {
        Calendar cal = getCalenderFromMonthAndYear(month1, year1);
        Calendar cal2 = getCalenderFromMonthAndYear(month2, year2);
        return cal.after(cal2);
    }

    /**
     * Create a Calender set to a date which is specified by a month and a year.
     *
     * @param month month of the date
     * @param year  year of the date
     * @return Calender set to the date
     */
    public static Calendar getCalenderFromMonthAndYear(int month, int year) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);

        return cal;
    }

    /**
     * @return Month (0-based) of today's date
     */
    public static int getMonth(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    /**
     * @return Year of today's date
     */
    public static int getYear(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
}