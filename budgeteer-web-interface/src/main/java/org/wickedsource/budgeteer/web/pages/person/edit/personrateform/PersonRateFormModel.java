package org.wickedsource.budgeteer.web.pages.person.edit.personrateform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
class PersonRateFormDto implements Serializable {

    private long personId;
    private List<BudgetBaseData> chosenBudgets = new ArrayList<>();
    private Money rate;
    private DateRange dateRange;

}
