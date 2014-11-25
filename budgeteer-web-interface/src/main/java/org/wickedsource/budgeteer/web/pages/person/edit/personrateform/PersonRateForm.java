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
        add(rateField);

        DateRangeInputField dateRangeField = new DateRangeInputField("dateRangeField", model(from(getModel()).getDateRange()));
        add(dateRangeField);

        DropDownChoice<BudgetBaseData> budgetChoice = new DropDownChoice<BudgetBaseData>("budgetField", model(from(getModel()).getBudget()), getBudgetChoices());
        budgetChoice.setChoiceRenderer(new BudgetBaseDataChoiceRenderer());
        add(budgetChoice);

        add(new Button("submitButton"));
    }

    @Override
    protected void onSubmit() {
        PersonRate addedRate = getModelObject();
        boolean error = false;
        if (addedRate.getRate() == null) {
            error("Please provide a rate.");
            error = true;
        }
        if (addedRate.getBudget() == null) {
            error("Please provide a budget.");
            error = true;
        }
        if (addedRate.getDateRange() == null) {
            error("Please provide a date range.");
            error = true;
        }
        if (!error) {
            if (addedRate.getBudget() == ALL_BUDGETS) {
                List<BudgetBaseData> budgetList = budgetService.loadBudgetBaseDataForProject(BudgeteerSession.get().getProjectId());
                for (BudgetBaseData budget : budgetList) {
                    PersonRate rate = new PersonRate();
                    rate.setDateRange(addedRate.getDateRange());
                    rate.setBudget(budget);
                    rate.setRate(addedRate.getRate());
                    rateAdded(rate);
                }
            } else {
                rateAdded(addedRate);
            }
            setModel(new PersonRateModel(new PersonRate()));
        }
    }


    protected abstract void rateAdded(PersonRate rate);

    private List<? extends BudgetBaseData> getBudgetChoices() {
        List<BudgetBaseData> budgetList = budgetService.loadBudgetBaseDataForProject(BudgeteerSession.get().getProjectId());
        budgetList.add(0, ALL_BUDGETS);
        return budgetList;
    }

}
