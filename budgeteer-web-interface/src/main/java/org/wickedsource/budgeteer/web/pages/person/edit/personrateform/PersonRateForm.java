package org.wickedsource.budgeteer.web.pages.person.edit.personrateform;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.person.PersonRate;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.budget.BudgetBaseDataChoiceRenderer;
import org.wickedsource.budgeteer.web.components.daterange.DateRangeInputField;
import org.wickedsource.budgeteer.web.components.money.MoneyTextField;
import org.wickedsource.budgeteer.web.components.multiselect.MultiselectBehavior;

import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public abstract class PersonRateForm extends Form<PersonRateForm.PersonRateFormModel> {

    public static class PersonRateFormModel extends PersonRate{
        private ListModel<BudgetBaseData> chosenBudgets = new ListModel<BudgetBaseData>();

        public ListModel<BudgetBaseData> getChosenBudgets() {
            return chosenBudgets;
        }
    }

    @SpringBean
    private BudgetService budgetService;


    public PersonRateForm(String id) {
        super(id, model(from(new PersonRateFormModel())));
        Injector.get().inject(this);

        MoneyTextField rateField = new MoneyTextField("rateField", model(from(getModel()).getRate()));
        rateField.setRequired(true);
        add(rateField);

        DateRangeInputField dateRangeField = new DateRangeInputField("dateRangeField", model(from(getModel()).getDateRange()));
        dateRangeField.setRequired(true);
        add(dateRangeField);

        List<BudgetBaseData> possibleBudgets = budgetService.loadBudgetBaseDataForProject(BudgeteerSession.get().getProjectId());
        ListMultipleChoice<BudgetBaseData> budgetChoice =
                new ListMultipleChoice<BudgetBaseData>("budgetField", getModelObject().getChosenBudgets(), possibleBudgets, new IChoiceRenderer<BudgetBaseData>() {
                    @Override
                    public Object getDisplayValue(BudgetBaseData object) {
                        return object.getName();
                    }

                    @Override
                    public String getIdValue(BudgetBaseData object, int index) {
                        return "" + object.getId();
                    }
                });

        budgetChoice.setRequired(true);
        budgetChoice.setChoiceRenderer(new BudgetBaseDataChoiceRenderer());

        budgetChoice.add(new MultiselectBehavior(MultiselectBehavior.getRecommendedOptions()));

        add(budgetChoice);

        add(new Button("submitButton"));
    }

    @Override
    protected void onSubmit() {
        PersonRateFormModel addedPersonRate = getModelObject();
        for (BudgetBaseData budget : addedPersonRate.getChosenBudgets().getObject()) {
            rateAdded(new PersonRate(addedPersonRate.getRate(), budget, addedPersonRate.getDateRange()));
        }
        addedPersonRate.getChosenBudgets().getObject().clear();
        addedPersonRate.reset();
    }

    protected abstract void rateAdded(PersonRate rate);

}
