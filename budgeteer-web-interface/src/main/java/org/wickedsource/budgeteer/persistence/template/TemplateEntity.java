package org.wickedsource.budgeteer.persistence.template;


import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.person.PersonEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.service.template.Template;

import javax.persistence.*;
import java.util.Date;

/*@Entity
@Table(name="TEMPLATE")*/
@Data
public class TemplateEntity {

/*    @Id
    @GeneratedValue*/
    private long id;
    private long projectId;

    private final String name;
    private final String description;
    private final XSSFWorkbook wb;



    TemplateEntity(long id, String name, String description, XSSFWorkbook workbook, long projectID){
        this.id = id;
        this.name = name;
        this.description = description;
        this.wb = workbook;
        this.projectId = projectID;
    }

    public Template getTemplate(){
        return new Template(id, name, description, wb, projectId);
    }
}
