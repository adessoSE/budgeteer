package org.wickedsource.budgeteer.web.pages.administration;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.wickedsource.budgeteer.service.DateRange;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
public class Project implements Serializable{
    private long projectId;
    private DateRange dateRange;
    private String name;

    public Project(long projectId, Date projectStart, Date projectEnd, String name){
        this.projectId = projectId;
        this.name = name;
        this.dateRange = new DateRange(projectStart, projectEnd);
    }
}
