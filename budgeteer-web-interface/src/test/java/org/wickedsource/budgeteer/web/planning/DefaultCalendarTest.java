package org.wickedsource.budgeteer.web.planning;

import org.joda.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.joda.time.DateTimeConstants.*;

class DefaultCalendarTest {

	@Test
	void testGetDays() throws Exception {
		DefaultCalendar cal = createDefaultCalendar();

		Assertions.assertEquals(365, cal.getNumberOfDays());
		Assertions.assertEquals(261, cal.getNumberOfWorkingDays());
		Assertions.assertEquals(104, cal.getNumberOfHolidays());

		LocalDate firstDay = cal.getStart();
		Assertions.assertEquals(2015, firstDay.getYear());
		Assertions.assertEquals(JANUARY, firstDay.getMonthOfYear());
		Assertions.assertEquals(1, firstDay.getDayOfMonth());

		LocalDate lastDay = cal.getEnd();
		Assertions.assertEquals(2015, lastDay.getYear());
		Assertions.assertEquals(DECEMBER, lastDay.getMonthOfYear());
		Assertions.assertEquals(31, lastDay.getDayOfMonth());
	}

	@Test
	void testGetDaysWithDeNwHolidays() throws Exception {
		DefaultCalendar cal = createDefaultCalendar();
		cal.setHolidayManager(new HolidayConfiguration("de", "nw"));

		Assertions.assertEquals(365, cal.getNumberOfDays());
		Assertions.assertEquals(251, cal.getNumberOfWorkingDays());
		Assertions.assertEquals(114, cal.getNumberOfHolidays());

		LocalDate firstDay = cal.getStart();
		Assertions.assertEquals(2015, firstDay.getYear());
		Assertions.assertEquals(JANUARY, firstDay.getMonthOfYear());
		Assertions.assertEquals(1, firstDay.getDayOfMonth());

		LocalDate lastDay = cal.getEnd();
		Assertions.assertEquals(2015, lastDay.getYear());
		Assertions.assertEquals(DECEMBER, lastDay.getMonthOfYear());
		Assertions.assertEquals(31, lastDay.getDayOfMonth());
	}

	@Test
	void testGetNumberOfWorkingDaysInPeriod() {
		DefaultCalendar cal = createDefaultCalendar();
		LocalDate start = new LocalDate(2015, JULY, 9);
		LocalDate end = new LocalDate(2015, JULY, 15);
		TimePeriod period = new TimePeriod(start, end);

		Assertions.assertEquals(5, cal.getNumberOfWorkingDaysInPeriod(period));
	}

	private DefaultCalendar createDefaultCalendar() {
		return DefaultCalendar.calendarYear(2015);
	}
}
