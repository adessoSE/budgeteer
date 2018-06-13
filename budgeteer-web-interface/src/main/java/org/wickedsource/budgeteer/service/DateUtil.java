package org.wickedsource.budgeteer.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DateUtil {

	@Autowired private DateProvider dateProvider;

	/** Returns the current date minus a number of weeks. */
	public Date weeksAgo(int numberOfWeeks) {
		Calendar c = Calendar.getInstance();
		c.setTime(dateProvider.currentDate());
		c.add(
				Calendar.WEEK_OF_YEAR,
				-numberOfWeeks + 1); // +1, because we want to have the current week included
		return c.getTime();
	}

	/** Returns the current date minus a number of months. */
	public Date monthsAgo(int numberOfMonths) {
		Calendar c = Calendar.getInstance();
		c.setTime(dateProvider.currentDate());
		c.add(
				Calendar.MONTH,
				-numberOfMonths + 1); // +1, because we want to have the current month included
		return c.getTime();
	}

	/** Returns the current date minus a number of days. */
	public Date daysAgo(int numberOfDays) {
		Calendar c = Calendar.getInstance();
		c.setTime(dateProvider.currentDate());
		c.add(
				Calendar.DAY_OF_YEAR,
				-numberOfDays + 1); // +1, because we want to have the current day included
		return c.getTime();
	}

	/**
	 * Returns whether the date is between the start and end of the dateRange
	 *
	 * @param d date to be checked
	 * @param dateRange
	 * @return true if the date d is in the given dateRange
	 */
	private static boolean isDateInDateRange(Date d, DateRange dateRange) {
		return d.compareTo(dateRange.getStartDate()) >= 0 && d.compareTo(dateRange.getEndDate()) <= 0;
	}

	/** Checks whether the two DateRanges are overlapping (ore one contains the other) */
	public static boolean isDateRangeOverlapping(DateRange d1, DateRange d2) {
		return ((isDateInDateRange(d1.getStartDate(), d2) || isDateInDateRange(d1.getEndDate(), d2))
				|| (isDateInDateRange(d2.getStartDate(), d1) && isDateInDateRange(d2.getEndDate(), d1)));
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

	/** Returns a list of the current year +- range */
	public static List<Integer> getCurrentYears(int range) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		int currentYear = cal.get(Calendar.YEAR);
		List<Integer> result = new LinkedList<Integer>();
		for (int i = 0; i < range * 2; i++) {
			result.add(currentYear - range + i);
		}
		return result;
	}
}
