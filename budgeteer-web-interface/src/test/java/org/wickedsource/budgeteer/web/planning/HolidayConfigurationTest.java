package org.wickedsource.budgeteer.web.planning;

import org.joda.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

class HolidayConfigurationTest {

	private static final String GERMANY = "de";
	private static final String NORTHRHINE_WESTFALIA = "nw";
	private HolidayConfiguration holidayManager;

	@BeforeEach
	void init() {
		holidayManager = new HolidayConfiguration(GERMANY, NORTHRHINE_WESTFALIA);
	}

	@Test
	void christmasIsOfficialGermanHoliday() {
		LocalDate christmas = new LocalDate(2015, 12, 25);
		assertTrue(holidayManager.checkOfficialHoliday(christmas));
	}

	@Test
	void reformationDayIsNoGermanHoliday() {
		LocalDate reformationDay = new LocalDate(2015, 10, 31);
		assertFalse(holidayManager.checkHoliday(reformationDay));
	}

	@Test
	void allSaintsIsOfficialNorthrhineWestfaliaHoliday() {
		LocalDate allSaints = new LocalDate(2015, 11, 1);
		assertTrue(holidayManager.checkHoliday(allSaints));
	}

	@Test
	void christmasEvesIsUnofficialGermanHoliday() {
		LocalDate christmasEve = new LocalDate(2015, 12, 24);
		assertTrue(holidayManager.checkUnofficialHoliday(christmasEve));
	}
}
