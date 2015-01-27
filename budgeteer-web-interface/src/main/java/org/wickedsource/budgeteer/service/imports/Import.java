package org.wickedsource.budgeteer.service.imports;

import lombok.Data;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

@Data
public class Import {

    private long id;
    private Date importDate;
    private String importType;
    private Date startDate;
    private Date endDate;


    /**
     * Returns a date with only the date-information available. Time-information are truncated
     * @return date-information of the startDate
     */
    public Date getStartDate() {
        return DateUtils.truncate(startDate, Calendar.DATE);
    }

    /**
     * Returns a date with only the date-information available. Time-information are truncated
     * @return date-information of the endDate
     */
    public Date getEndDate() {
        return DateUtils.truncate(endDate, Calendar.DATE);
    }
}
