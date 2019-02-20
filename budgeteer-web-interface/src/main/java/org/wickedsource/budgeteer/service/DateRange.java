package org.wickedsource.budgeteer.service;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateRange implements Serializable {

    private Date startDate;

    private Date endDate;

    @Setter
    @Getter
    private static SimpleDateFormat formatter = new SimpleDateFormat();

    public DateRange(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String toString() {
        return (startDate != null ? formatter.format(startDate) : "undefined") + " - " + (endDate != null ? formatter.format(endDate) : "undefined");
    }

    /**
     * Get the number of days of the DateRange
     *
     * @return number of days of the DateRange
     */
    public int getNumberOfDays() {
        Date start = DateUtil.getMidnightOfDate(startDate);
        Date end = DateUtil.getMidnightOfDate(endDate);

        long milliseconds = end.getTime() - start.getTime();
        double daysFactor = 60 * 60 * 1000 * 24;

        // Cast the milliseconds to full days
        return (int) Math.round(milliseconds / daysFactor);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}