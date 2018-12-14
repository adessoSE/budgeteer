package de.adesso.budgeteer.web.pages.person.edit.personrateform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import de.adesso.budgeteer.service.DateRange;
import de.adesso.budgeteer.service.budget.BudgetBaseData;

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
