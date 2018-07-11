package org.wickedsource.budgeteer.web.pages.person.edit.personrateform;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.extensions.markup.html.form.select.SelectOptions;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.person.PersonRate;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.daterange.DateRangeInputField;
import org.wickedsource.budgeteer.web.components.listMultipleChoiceWithGroups.OptionGroup;
import org.wickedsource.budgeteer.web.components.money.MoneyTextField;
import org.wickedsource.budgeteer.web.components.multiselect.MultiselectBehavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public abstract class PersonRateForm extends Form<PersonRateForm.PersonRateFormModel> {

    public static class PersonRateFormModel extends PersonRate {
        private Model<ArrayList<BudgetBaseData>> chosenBudgets = new Model<>(new ArrayList<>());

        public Model<ArrayList<BudgetBaseData>> getChosenBudgets() {
            return chosenBudgets;
        }
    }

    @SpringBean
    private BudgetService budgetService;


    public PersonRateForm(String id, long personId) {
        super(id, model(from(new PersonRateFormModel())));
        Injector.get().inject(this);

        MoneyTextField rateField = new MoneyTextField("rateField", model(from(getModel()).getRate()));
        rateField.setRequired(true);
        add(rateField);

        DateRangeInputField dateRangeField = new DateRangeInputField("dateRangeField", model(from(getModel()).getDateRange()), DateRangeInputField.DROP_LOCATION.UP);
        dateRangeField.setRequired(true);
        add(dateRangeField);

        List<OptionGroup<BudgetBaseData>> possibleBudgets = budgetService.getPossibleBudgetDataForPersonAndProject(BudgeteerSession.get().getProjectId(), personId);
        addBudgetsDropdown(possibleBudgets);

        add(new Button("submitButton"));
    }

    @Override
    protected void onSubmit() {
        PersonRateFormModel addedPersonRate = getModelObject();
        for(BudgetBaseData budget : addedPersonRate.getChosenBudgets().getObject()) {
            List<PersonRate> overlappingRate = getOverlappingRates(addedPersonRate.getDateRange(), budget);
            if (overlappingRate == null || overlappingRate.isEmpty()) {
                rateAdded(new PersonRate(addedPersonRate.getRate(), budget, addedPersonRate.getDateRange()));
            } else {
                StringBuilder overlappingEntryNames = new StringBuilder();
                overlappingEntryNames.append(System.getProperty("line.separator"));
                for (int i = 0; i < overlappingRate.size(); i++) {
                    PersonRate p = overlappingRate.get(i);
                    overlappingEntryNames.append(p.getBudget().getName());
                    overlappingEntryNames.append(" ");
                    overlappingEntryNames.append(p.getDateRange().toString());
                    if (i < overlappingRate.size() - 1) {
                        overlappingEntryNames.append(",");
                        overlappingEntryNames.append(System.getProperty("line.separator"));
                    }
                }
                error(String.format(getString("personRateForm.overlappingRates"), overlappingEntryNames.toString()));
                return;
            }
        }
        addedPersonRate.getChosenBudgets().getObject().clear();
        addedPersonRate.reset();
    }

    protected abstract void rateAdded(PersonRate rate);

    /**
     * Returns a List<PersonRate> where the startDate or the endDate is in the given dateRange (alternatively a empty List)
     * @param dateRange the dateRange to be checked
     * @return a List of PersonRates (if there are no PersonRates that end or start in the dateRange, a empty List will be returned)
     */
    protected abstract List<PersonRate> getOverlappingRates(DateRange dateRange, BudgetBaseData budget);

    /**
     * This methods adds a dropdown menu for the available budgets.
     * @param possibleBudgets A list of grouped possible budgets
     */
    private void addBudgetsDropdown(List<OptionGroup<BudgetBaseData>> possibleBudgets){
        Select<ArrayList<BudgetBaseData>> select = new Select<>("select", getModelObject().getChosenBudgets());
        HashMap<String, String> multiselectOptions = MultiselectBehavior.getRecommendedOptions();
        multiselectOptions.put("includeSelectAllOption","false");
        add(select.setRequired(true).add(new MultiselectBehavior(multiselectOptions)));
        RepeatingView rv = new RepeatingView("repeatingView");
        rv.setOutputMarkupId(true);
        rv.setOutputMarkupPlaceholderTag(true);
        select.add(rv);
        for(OptionGroup<BudgetBaseData> group : possibleBudgets){
            WebMarkupContainer overOptGroup = new WebMarkupContainer(rv.newChildId());
            rv.add(overOptGroup);
            WebMarkupContainer optGroup = new WebMarkupContainer("optGroup");
            overOptGroup.add(optGroup);
            optGroup.add(new AttributeModifier("label", new Model<>(group.getGroupNameResourceKey())));
            Model<ArrayList<BudgetBaseData>> a = new Model<>(new ArrayList<>(group.getOptions()));
            optGroup.add(
                    new SelectOptions<>("selectOptions", a, new IOptionRenderer<BudgetBaseData>() {
                        @Override
                        public String getDisplayValue(BudgetBaseData object) {
                            return object.getName();
                        }
                        @Override
                        public IModel<BudgetBaseData> getModel(BudgetBaseData value) {
                            return new Model<>(value);
                        }
                    })
            );
        }
    }
}
