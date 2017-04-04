package org.wickedsource.budgeteer.web.planning;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import static org.joda.time.DateTimeConstants.*;

public class DefaultCalendarTest {

	@Test
	public void testGetDays() throws Exception {
		DefaultCalendar cal = createDefaultCalendar();

		Assert.assertEquals(365, cal.getNumberOfDays());
		Assert.assertEquals(261, cal.getNumberOfWorkingDays());
		Assert.assertEquals(104, cal.getNumberOfHolidays());

		LocalDate firstDay = cal.getStart();
		Assert.assertEquals(2015, firstDay.getYear());
		Assert.assertEquals(JANUARY, firstDay.getMonthOfYear());
		Assert.assertEquals(1, firstDay.getDayOfMonth());

		LocalDate lastDay = cal.getEnd();
		Assert.assertEquals(2015, lastDay.getYear());
		Assert.assertEquals(DECEMBER, lastDay.getMonthOfYear());
		Assert.assertEquals(31, lastDay.getDayOfMonth());
	}

	@Test
	public void testGetDaysWithDeNwHolidays() throws Exception {
		DefaultCalendar cal = createDefaultCalendar();
		cal.setHolidayManager(new HolidayConfiguration("de", "nw"));

		Assert.assertEquals(365, cal.getNumberOfDays());
		Assert.assertEquals(251, cal.getNumberOfWorkingDays());
		Assert.assertEquals(114, cal.getNumberOfHolidays());

		LocalDate firstDay = cal.getStart();
		Assert.assertEquals(2015, firstDay.getYear());
		Assert.assertEquals(JANUARY, firstDay.getMonthOfYear());
		Assert.assertEquals(1, firstDay.getDayOfMonth());

		LocalDate lastDay = cal.getEnd();
		Assert.assertEquals(2015, lastDay.getYear());
		Assert.assertEquals(DECEMBER, lastDay.getMonthOfYear());
		Assert.assertEquals(31, lastDay.getDayOfMonth());
	}

	@Test
	public void testGetNumberOfWorkingDaysInPeriod() {
		DefaultCalendar cal = createDefaultCalendar();
		LocalDate start = new LocalDate(2015, JULY, 9);
		LocalDate end = new LocalDate(2015, JULY, 15);
		TimePeriod period = new TimePeriod(start, end);

		Assert.assertEquals(5, cal.getNumberOfWorkingDaysInPeriod(period));
	}

	private DefaultCalendar createDefaultCalendar() {
		return DefaultCalendar.calendarYear(2015);
	}
}
