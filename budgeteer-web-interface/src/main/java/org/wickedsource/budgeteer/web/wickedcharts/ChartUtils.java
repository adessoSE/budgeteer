package org.wickedsource.budgeteer.web.wickedcharts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ChartUtils {

    /**
     * Creates a list of labels for the last numberOfWeeks weeks, including the current week. This list can be used for labels in a chart.
     *
     * @param numberOfWeeks   the number of weeks to go back into the past.
     * @param weekLabelFormat the format of the week labels. Should contain "%s" as a placeholder for the week number.
     * @return list of week labels.
     */
    public static List<String> getWeekLabels(int numberOfWeeks, String weekLabelFormat) {
        List<String> labels = new ArrayList<String>();
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
}
