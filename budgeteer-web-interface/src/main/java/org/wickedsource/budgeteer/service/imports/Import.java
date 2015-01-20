package org.wickedsource.budgeteer.service.imports;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class Import {

    private long id;

    private Date importDate;

    private String importType;

    private Date startDate;

    private Date endDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }

    /**
     * Returns a date with only the date-information available. Time-information are truncated
     * @return date-information of the startDate
     */
    public Date getStartDate() {
        return DateUtils.truncate(startDate, Calendar.DATE);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns a date with only the date-information available. Time-information are truncated
     * @return date-information of the endDate
     */
    public Date getEndDate() {
        return DateUtils.truncate(endDate, Calendar.DATE);
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
