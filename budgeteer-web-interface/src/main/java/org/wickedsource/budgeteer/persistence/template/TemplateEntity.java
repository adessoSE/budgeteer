package org.wickedsource.budgeteer.persistence.template;


import lombok.Data;
import lombok.experimental.Accessors;
import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.person.PersonEntity;
import org.wickedsource.budgeteer.service.template.Template;

import javax.persistence.*;
import java.util.Date;

/*@Entity
@Table(name="TEMPLATE")*/
@Data
@Accessors(chain = true)
public class TemplateEntity {

    @Id
    @GeneratedValue
    private long id;

    private String name;
    private String description;

    public Template getTemplate(){
        return new Template().setName(name).setDescription(description);
    }
}
