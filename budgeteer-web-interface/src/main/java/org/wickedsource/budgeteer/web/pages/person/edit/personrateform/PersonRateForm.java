package org.wickedsource.budgeteer.web.pages.person.edit.personrateform;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.person.PersonRate;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.budget.BudgetBaseDataChoiceRenderer;
import org.wickedsource.budgeteer.web.components.daterange.DateRangeInputField;
import org.wickedsource.budgeteer.web.components.money.MoneyTextField;

import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public abstract class PersonRateForm extends Form<PersonRate> {

    @SpringBean
    private BudgetService budgetService;

    private final BudgetBaseData ALL_BUDGETS = new BudgetBaseData(0, getString("allBudgets"));

    public PersonRateForm(String id) {
        super(id, model(from(new PersonRate())));
        Injector.get().inject(this);

        MoneyTextField rateField = new MoneyTextField("rateField", model(from(getModel()).getRate()));
        rateField.setRequired(true);
        add(rateField);

        DateRangeInputField dateRangeField = new DateRangeInputField("dateRangeField", model(from(getModel()).getDateRange()));
        dateRangeField.setRequired(true);
        add(dateRangeField);

        DropDownChoice<BudgetBaseData> budgetChoice = new DropDownChoice<BudgetBaseData>("budgetField", model(from(getModel()).getBudget()), getBudgetChoices());
        budgetChoice.setRequired(true);
        budgetChoice.setChoiceRenderer(new BudgetBaseDataChoiceRenderer());
        add(budgetChoice);

        add(new Button("submitButton"));
    }

    @Override
    protected void onSubmit() {
        PersonRate addedRate = getModelObject();
        if (addedRate.getBudget() == ALL_BUDGETS) {
            List<BudgetBaseData> budgetList = budgetService.loadBudgetBaseDataForProject(BudgeteerSession.get().getProjectId());
            for (BudgetBaseData budget : budgetList) {
                rateAdded(new PersonRate(addedRate.getRate(), budget, addedRate.getDateRange()));
            }
        } else {
            rateAdded(new PersonRate(addedRate.getRate(), addedRate.getBudget(), addedRate.getDateRange()));
        }
        addedRate.reset();
    }


    protected abstract void rateAdded(PersonRate rate);

    private List<? extends BudgetBaseData> getBudgetChoices() {
        List<BudgetBaseData> budgetList = budgetService.loadBudgetBaseDataForProject(BudgeteerSession.get().getProjectId());
        budgetList.add(0, ALL_BUDGETS);
        return budgetList;
    }

}
