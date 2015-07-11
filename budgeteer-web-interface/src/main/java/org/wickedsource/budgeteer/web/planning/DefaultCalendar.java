package org.wickedsource.budgeteer.web.planning;

import java.util.*;

/**
 * Standard calendar in which only weekends are marked as holidays.
 */
public class DefaultCalendar extends org.wickedsource.budgeteer.web.planning.Calendar {

    /**
     * Mapping the first millisecond of each day to a Day object.
     */
    private Map<Long, Day> days = new HashMap<>();

    private Day firstDay;

    private Day lastDay;

    private int numberOfWorkingDays;

    private int numberOfHolidays;

    public DefaultCalendar(Date start, Date end) {

        java.util.Calendar startCal = java.util.Calendar.getInstance();
        startCal.setTime(start);
        clear(startCal);

        java.util.Calendar endCal = java.util.Calendar.getInstance();
        endCal.setTime(end);
        clear(endCal);

        firstDay = addDay(startCal);
        startCal.add(java.util.Calendar.DAY_OF_YEAR, 1);

        Day previousDay = firstDay;
        while (startCal.before(endCal)) {
            Day day = addDay(startCal);
            startCal.add(java.util.Calendar.DAY_OF_YEAR, 1);
            previousDay.setNextDay(day);
            previousDay = day;
        }
        lastDay = addDay(endCal);
    }

    private Day addDay(java.util.Calendar date) {
        int dayOfWeek = date.get(java.util.Calendar.DAY_OF_WEEK);
        boolean isHoliday = false;
        if (dayOfWeek == java.util.Calendar.SATURDAY
                || dayOfWeek == java.util.Calendar.SUNDAY) {
            isHoliday = true;
            this.numberOfHolidays++;
        }else{
            this.numberOfWorkingDays++;
        }
        Day day = new Day(date.getTime(), isHoliday);
        days.put(date.getTimeInMillis(), day);
        return day;
    }

    private void clear(java.util.Calendar calendar) {
        calendar.clear(java.util.Calendar.HOUR_OF_DAY);
        calendar.clear(java.util.Calendar.MINUTE);
        calendar.clear(java.util.Calendar.SECOND);
        calendar.clear(java.util.Calendar.MILLISECOND);
    }

    @Override
    public Day getFirstDay() {
        return firstDay;
    }

    @Override
    public Day getLastDay() {
        return lastDay;
    }

    @Override
    public int getNumberOfWorkingDays() {
        return this.numberOfWorkingDays;
    }

    @Override
    public int getNumberOfHolidays() {
        return this.numberOfHolidays;
    }

    @Override
    public int getNumberOfDays() {
        return this.numberOfWorkingDays + this.numberOfHolidays;
    }

    @Override
    public int getNumberOfWorkingDaysInPeriod(TimePeriod period) {
        java.util.Calendar startCal = period.getStartCalendar();
        clear(startCal);
        java.util.Calendar endCal = period.getEndCalendar();
        clear(endCal);

        int numberOfWorkingDays = 0;

        Day currentDay = days.get(startCal.getTimeInMillis());
        if(currentDay == null){
            throw new RuntimeException("Start of TimePeriod is not in the date range of this calendar!");
        }
        while(currentDay != null && currentDay.getDate().getTime() <= endCal.getTimeInMillis()){
            if(!currentDay.isHoliday()){
                numberOfWorkingDays++;
            }
            currentDay = currentDay.nextDay();
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
