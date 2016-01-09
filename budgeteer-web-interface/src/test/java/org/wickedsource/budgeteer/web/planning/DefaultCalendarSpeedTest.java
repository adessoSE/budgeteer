package org.wickedsource.budgeteer.web.planning;

import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultCalendarSpeedTest {

	Logger logger = LoggerFactory.getLogger(DefaultCalendarSpeedTest.class);

	public static final int TIMES = 10000;

	@Test
	public void findsHolidaysInFeasibleTime() {

		DefaultCalendar calWithoutHolidays = DefaultCalendar.calendarYear(2015);

		DefaultCalendar calWithHolidays = DefaultCalendar.calendarYear(2015);
		calWithHolidays.setHolidayManager(new HolidayConfiguration("de", "nw"));

		List<TimePeriod> periods = Arrays.asList(new TimePeriod(new LocalDate(2015, 3, 1), new LocalDate(2015, 3, 31)),
				new TimePeriod(new LocalDate(2015, 4, 30), new LocalDate(2015, 4, 30)),
				new TimePeriod(new LocalDate(2015, 5, 3), new LocalDate(2015, 5, 7)),
				new TimePeriod(new LocalDate(2015, 7, 19), new LocalDate(2015, 8, 6)));

		long beforeStart = System.currentTimeMillis();

		for (int i = 0; i < TIMES; i++) {
			calWithoutHolidays.getNumberOfWorkingDays(periods);
		}

		long inBetween = System.currentTimeMillis();

		for (int i = 0; i < TIMES; i++) {
			calWithHolidays.getNumberOfWorkingDays(periods);
		}

		long after = System.currentTimeMillis();

		long millisWith = after - inBetween;
		logger.info("to calculate working days with holidays " + TIMES + " times takes " + millisWith + "ms");
		long millisWithout = inBetween - beforeStart;
		logger.info("to calculate working days without holidays " + TIMES + " times takes " + millisWithout + "ms");

		Assert.assertTrue(Double.compare(millisWithout * 1.5, millisWith) > 0);
	}
}
