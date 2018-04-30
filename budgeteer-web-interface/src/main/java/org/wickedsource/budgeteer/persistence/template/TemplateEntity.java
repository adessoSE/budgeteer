package org.wickedsource.budgeteer.persistence.template;


import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.person.PersonEntity;
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

    private final String name;
    private final String description;
    private final XSSFWorkbook wb;

    TemplateEntity(long id, String name, String description, XSSFWorkbook workbook){
        this.id = id;
        this.name = name;
        this.description = description;
        this.wb = workbook;
    }

    public Template getTemplate(){
        return new Template(id, name, description, wb);
    }
}
