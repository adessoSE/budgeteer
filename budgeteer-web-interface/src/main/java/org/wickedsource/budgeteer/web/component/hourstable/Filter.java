package org.wickedsource.budgeteer.web.component.hourstable;

import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.people.DateRange;
import org.wickedsource.budgeteer.service.people.PersonBaseData;

import java.io.Serializable;

public class Filter implements Serializable{

    private PersonBaseData selectedPerson;

    private BudgetBaseData selectedBudget;

    private DateRange selectedDaterange;

    public PersonBaseData getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(PersonBaseData selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public BudgetBaseData getSelectedBudget() {
        return selectedBudget;
    }

    public void setSelectedBudget(BudgetBaseData selectedBudget) {
        this.selectedBudget = selectedBudget;
    }

    public DateRange getSelectedDaterange() {
        return selectedDaterange;
    }

    public void setSelectedDaterange(DateRange selectedDaterange) {
        this.selectedDaterange = selectedDaterange;
    }
}
