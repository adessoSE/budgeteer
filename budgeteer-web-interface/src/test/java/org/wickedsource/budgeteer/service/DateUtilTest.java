package org.wickedsource.budgeteer.service;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;

public class DateUtilTest {

    @Test
    public void isDateRangeOverlapping() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        DateRange d1 = new DateRange(formatter.parse("20.02.2015"), formatter.parse("25.02.2015"));

        //date1 starts in date2
        DateRange d2 = new DateRange(formatter.parse("20.01.2015"), formatter.parse("20.02.2015"));
        assertEquals(DateUtil.isDateRangeOverlapping(d1, d2), true);

        //date1 ends in date2
        d2 = new DateRange(formatter.parse("24.02.2015"), formatter.parse("20.03.2015"));
        assertEquals(DateUtil.isDateRangeOverlapping(d1, d2), true);

        //date1 contains date2
        d2 = new DateRange(formatter.parse("21.02.2015"), formatter.parse("22.02.2015"));
        assertEquals(DateUtil.isDateRangeOverlapping(d1, d2), true);

        //date2 contains date1
        d2 = new DateRange(formatter.parse("19.02.2015"), formatter.parse("26.02.2015"));
        assertEquals(DateUtil.isDateRangeOverlapping(d1, d2), true);

        //date1 after date2
        d2 = new DateRange(formatter.parse("26.02.2015"), formatter.parse("22.03.2015"));
        assertEquals(DateUtil.isDateRangeOverlapping(d1, d2), false);
    }

}
