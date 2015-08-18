package org.wickedsource.budgeteer.web.planning;

import java.util.List;

public abstract class Calendar {

    public abstract Day getFirstDay();

    public abstract Day getLastDay();

    public abstract int getNumberOfWorkingDays();

    public abstract int getNumberOfHolidays();

    public abstract int getNumberOfDays();

    /**
     * Returns the number of days in the given TimePeriod (including start and end day).
     */
    public abstract int getNumberOfWorkingDaysInPeriod(TimePeriod period);

    public abstract int getNumberOfWorkingDays(List<TimePeriod> absences);

}
