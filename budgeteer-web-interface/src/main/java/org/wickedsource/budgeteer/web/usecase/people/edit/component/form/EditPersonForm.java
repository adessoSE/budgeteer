package org.wickedsource.budgeteer.web.usecase.people.edit.component.form;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.people.PersonRate;
import org.wickedsource.budgeteer.service.people.PersonWithRates;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.usecase.base.component.daterange.DateRangeInputField;

import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class EditPersonForm extends Form<PersonWithRates> {

    @SpringBean
    private BudgetService budgetService;

    private PersonRate addedRate = new PersonRate();

    /**
     * Use this constructor to create a form that creates a new user.
     */
    public EditPersonForm(String id) {
        super(id, new PersonWithRatesModel(new PersonWithRates()));
        Injector.get().inject(this);
        addComponents();
    }

    /**
     * Use this constructor to create a form that edits and existing user.
     */
    public EditPersonForm(String id, PersonWithRates person) {
        super(id, new PersonWithRatesModel(person));
        addComponents();
    }

    private void addComponents() {
        add(new TextField<String>("name", model(from(getModel()).getName())));
        add(new TextField<String>("importKey", model(from(getModel()).getImportKey())));
        add(createRatesList("ratesList"));
        add(new NumberTextField<Double>("rateField", model(from(addedRate).getRate())));
        add(new DateRangeInputField("dateRangeField", model(from(addedRate).getDateRange())));

        DropDownChoice<BudgetBaseData> budgetChoice = new DropDownChoice<BudgetBaseData>("budgetField", model(from(addedRate).getBudget()), getBudgetChoices());
        budgetChoice.setChoiceRenderer(new BudgetBaseDataChoiceRenderer());
        add(budgetChoice);

        Button addButton = new Button("addButton") {
            @Override
            public void onSubmit() {
                EditPersonForm.this.getModelObject().getRates().add(PersonRate.copy(addedRate));
            }
        };
        add(addButton);
    }

    private List<? extends BudgetBaseData> getBudgetChoices() {
        return budgetService.loadBudgetBaseData(BudgeteerSession.get().getLoggedInUserId());
    }

    private ListView<PersonRate> createRatesList(String id) {
        return new ListView<PersonRate>(id, model(from(getModel()).getRates())) {
            @Override
            protected void populateItem(final ListItem<PersonRate> item) {
                item.add(new Label("rate", model(from(item.getModel()).getRate())));
                item.add(new Label("budget", model(from(item.getModel()).getBudget().getName())));
                item.add(new Label("startDate", model(from(item.getModel()).getDateRange().getStartDate())));
                item.add(new Label("endDate", model(from(item.getModel()).getDateRange().getEndDate())));
                Button deleteButton = new Button("deleteButton") {
                    @Override
                    public void onSubmit() {
                        EditPersonForm.this.getModelObject().getRates().remove(item.getModelObject());
                    }
                };
                deleteButton.setDefaultFormProcessing(false);
                item.add(deleteButton);
            }

            @Override
            protected ListItem<PersonRate> newItem(int index, IModel<PersonRate> itemModel) {
                // wrap into Model that can be used with LazyModel
                return super.newItem(index, new RatesModel(itemModel));
            }
        };
    }

}
