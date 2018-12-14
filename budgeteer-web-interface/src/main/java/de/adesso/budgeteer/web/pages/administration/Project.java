package de.adesso.budgeteer.web.pages.administration;


import de.adesso.budgeteer.service.DateRange;
import lombok.AllArgsConstructor;
import lombok.Data;

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
