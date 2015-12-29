package org.wickedsource.budgeteer.web.planning;

import lombok.Getter;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.ReadablePeriod;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.joda.time.DateTimeConstants.*;
import static org.joda.time.Period.years;

/**
 * Calendar in which weekends and local holidays are marked as holidays.
 */
public class DefaultCalendar extends Calendar {

    /**
     * Mapping the first millisecond of each day to a Day object.
     */
    private Map<Long, Day> days;

    @Getter
    private Day firstDay;

    @Getter
    private Day lastDay;

    @Getter
    private int numberOfWorkingDays;

    @Getter
    private int numberOfHolidays;

    public DefaultCalendar(Date start, Date end) {
        this(new LocalDate(start), new LocalDate(end), true);
    }

    public DefaultCalendar(LocalDate start, ReadablePeriod period) {
        this(start, start.plus(period), false);
    }

    public DefaultCalendar(LocalDate start, LocalDate end, boolean includingEnd) {
        if (!includingEnd) {
            end = end.minusDays(1);
        }
        if (start.isAfter(end))
            throw new IllegalArgumentException("end must not predate start");
        initialize(start, end);
    }

    public static DefaultCalendar calendarYear(int year) {
        return new DefaultCalendar(new LocalDate(year, JANUARY, 1), years(1));
    }

    private void initialize(LocalDate start, LocalDate end) {
        days = new HashMap<>();
        LocalDate day = start;
        Day current = initializeDay(start, null);
        firstDay = current;
        for (day = day.plusDays(1); !day.isAfter(end); day = day.plusDays(1)) {
            current = initializeDay(day, current);
        }
        lastDay = current;
    }

    private Day initializeDay(LocalDate date, Day last) {
        boolean weekend = checkDateIsOnWeekend(date);
        boolean holiday = false; // @todo checkHoliday(date); based on jollyday
        DateTime dayStart = date.toDateTimeAtStartOfDay();
        Day day = new Day(dayStart.toDate(), weekend || holiday);
        if (last != null) {
            last.setNextDay(day);
        }
        return recordDay(dayStart, day);
    }

    private boolean checkDateIsOnWeekend(LocalDate date) {
        switch (date.getDayOfWeek()) {
            case SATURDAY:
            case SUNDAY:
                return true;
            default:
                return false;
        }
    }

    private Day recordDay(DateTime dayStart, Day day) {
        days.put(dayStart.getMillis(), day);
        if (day.isHoliday()) {
            numberOfHolidays++;
        } else {
            numberOfWorkingDays++;
        }
        return day;
    }

    @Override
    public int getNumberOfDays() {
        return this.numberOfWorkingDays + this.numberOfHolidays;
    }

    @Override
    public int getNumberOfWorkingDaysInPeriod(TimePeriod period) {

        LocalDate start = new LocalDate(period.getStart());
        LocalDate end = new LocalDate(period.getEnd());

        int numberOfWorkingDays = 0;

        LocalDate date = start;
        for (; !date.isAfter(end); date = date.plusDays(1)) {
            Day day = days.get(date.toDateTimeAtStartOfDay().getMillis());
            if (day == null) {
                break;
            }
            if (!day.isHoliday()) {
                numberOfWorkingDays++;
            }
        }
        return numberOfWorkingDays;
    }

    @Override
    public int getNumberOfWorkingDays(List<TimePeriod> absences) {
        int workingDays = this.numberOfWorkingDays;
        for(TimePeriod absence : absences){
            workingDays -= getNumberOfWorkingDaysInPeriod(absence);
        }
        return workingDays;
    }
}
