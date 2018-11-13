package org.wickedsource.budgeteer.web.charts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ChartUtils {

    private ChartUtils(){

    }

    /**
     * Creates a list of labels for the last numberOfWeeks weeks, including the current week. This list can be used for labels in a chart.
     *
     * @param numberOfWeeks   the number of weeks to go back into the past.
     * @param weekLabelFormat the format of the week labels. Should contain "%s" as a placeholder for the week number.
     * @return list of week labels.
     */
    public static List<String> getWeekLabels(int numberOfWeeks, String weekLabelFormat) {
        List<String> labels = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        int currentWeek = c.get(Calendar.WEEK_OF_YEAR);
        for (int i = 0; i < numberOfWeeks; i++) {
            labels.add(String.format(weekLabelFormat, currentWeek));
            c.add(Calendar.WEEK_OF_YEAR, -1);
            currentWeek = c.get(Calendar.WEEK_OF_YEAR);
        }
        Collections.reverse(labels);
        return labels;
    }

    private final static DateFormat monthFormat = new SimpleDateFormat("MMM ''yy");

    /**
     * Creates a list of labels for the last numberOfMonths months, including the current month. This list can be used for labels in a chart.
     *
     * @param numberOfMonths the number of months to go back into the past.
     * @return list of month labels.
     */
    public static List<String> getMonthLabels(int numberOfMonths) {
        List<String> labels = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < numberOfMonths; i++) {
            labels.add(monthFormat.format(c.getTime()));
            c.add(Calendar.MONTH, -1);
        }
        Collections.reverse(labels);
        return labels;
    }
}
