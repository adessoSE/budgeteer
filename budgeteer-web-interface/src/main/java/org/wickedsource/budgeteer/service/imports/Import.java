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
    private Integer numberOfImportedFiles = 0;
    private Date startDate;
    private Date endDate;


    /**
     * Returns a date with only the date-information available. Time-information are truncated
     *
     * @return date-information of the startDate
     */
    public Date getStartDate() {
        if (startDate == null) {
            return null;
        } else {
            return DateUtils.truncate(startDate, Calendar.DATE);
        }
    }

    /**
     * Returns a date with only the date-information available. Time-information are truncated
     *
     * @return date-information of the endDate
     */
    public Date getEndDate() {
        if (endDate == null) {
            return null;
        } else {
            return DateUtils.truncate(endDate, Calendar.DATE);
        }
    }
}
