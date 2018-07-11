package org.wickedsource.budgeteer.web.pages.person.edit.personrateform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.wicket.model.Model;
import org.joda.money.Money;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
class PersonRateFormDto implements Serializable {

    private long personId;
    private Model<ArrayList<BudgetBaseData>> chosenBudgets = new Model<>(new ArrayList<>());
    private Money rate;
    private DateRange dateRange;

}
