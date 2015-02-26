package org.wickedsource.budgeteer.web.pages.administration;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.wickedsource.budgeteer.service.DateRange;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Data
@AllArgsConstructor
public class Project implements Serializable{
    private long projectId;
    private DateRange dateRange;
    private String name;

    public Project(long projectId, Date projectStart, Date projectEnd, String name){
        this.projectId = projectId;
        this.name = name;
        setDateRange(projectStart, projectEnd);
    }

    /**
     * If the projectStart == null it will be set to the first day of the year;
     * if the projectEnd == null it will be set to the last day of the current year
     */
    private DateRange normalizeDateRange(Date projectStart, Date projectEnd) {
        if(projectStart == null || projectEnd == null) {
            Calendar cal = new GregorianCalendar();
            if(projectStart == null){
                cal.setTime(new Date());
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.MONTH, 0);
                projectStart = cal.getTime();
            }
            if(projectEnd == null){
                cal.setTime(new Date());
                cal.set(Calendar.DAY_OF_MONTH, 31);
                cal.set(Calendar.MONTH, 11);
                projectEnd = cal.getTime();
            }
        }
        return new DateRange(projectStart, projectEnd);
    }

    public void setDateRange(Date projectStart, Date projectEnd){
        dateRange =  normalizeDateRange(projectStart, projectEnd);
    }
}
