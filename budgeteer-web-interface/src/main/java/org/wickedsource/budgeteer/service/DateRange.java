package org.wickedsource.budgeteer.service;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateRange implements Serializable {

    private Date startDate;

    private Date endDate;

    private static SimpleDateFormat formatter = new SimpleDateFormat("dd.mm.yy");

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

    public String toString(){
        return (startDate != null ? formatter.format(startDate) : "undefined") + " - " + (endDate != null ? formatter.format(endDate) : "undefined");
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