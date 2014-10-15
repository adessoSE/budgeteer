package org.wickedsource.budgeteer.web.usecase.people.edit.component.form;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.people.PersonRate;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.component.choicerenderer.BudgetBaseDataChoiceRenderer;
import org.wickedsource.budgeteer.web.component.daterange.DateRangeInputField;
import org.wickedsource.budgeteer.web.component.money.MoneyTextField;

import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public abstract class AddRateNestedForm extends Form<PersonRate> {

    @SpringBean
    private BudgetService budgetService;

    public AddRateNestedForm(String id) {
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

    private List<? extends BudgetBaseData> getBudgetChoices() {
        return budgetService.loadBudgetBaseDataForUser(BudgeteerSession.get().getLoggedInUserId());
    }

}
