package org.wickedsource.budgeteer.web.component.hourstable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.people.PersonBaseData;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.component.choicerenderer.BudgetBaseDataChoiceRenderer;
import org.wickedsource.budgeteer.web.component.choicerenderer.PersonBaseDataChoiceRenderer;
import org.wickedsource.budgeteer.web.usecase.base.component.daterange.DateRangeInputField;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class FilterPanel extends Panel {

    private boolean nameFilterEnabled = true;

    private boolean budgetFilterEnabled = true;

    private boolean daterangeFilterEnabled = true;

    public FilterPanel(String id) {
        super(id, model(from(new Filter())));
        add(createPersonSelect("personSelect"));
        add(createBudgetSelect("budgetSelect"));
        add(new DateRangeInputField("daterangeInput", model(from(getFilterModel()).getSelectedDaterange())));
    }

    private DropDownChoice<PersonBaseData> createPersonSelect(String id) {
        DropDownChoice<PersonBaseData> select = new DropDownChoice<PersonBaseData>(id, model(from(getFilterModel()).getSelectedPerson()), new PersonListModel(BudgeteerSession.get().getLoggedInUserId()));
        select.add(createSendFilterBehavior());
        select.setChoiceRenderer(new PersonBaseDataChoiceRenderer());
        return select;
    }

    private DropDownChoice<BudgetBaseData> createBudgetSelect(String id) {
        DropDownChoice<BudgetBaseData> select = new DropDownChoice<BudgetBaseData>(id, model(from(getFilterModel()).getSelectedBudget()), new BudgetListModel(BudgeteerSession.get().getLoggedInUserId()));
        select.add(createSendFilterBehavior());
        select.setChoiceRenderer(new BudgetBaseDataChoiceRenderer());
        return select;
    }

    private Behavior createSendFilterBehavior() {
        return new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                send(getPage(), Broadcast.BREADTH, getFilterModel().getObject());
                setResponsePage(getPage());
            }
        };
    }

    @SuppressWarnings("unchecked")
    public IModel<Filter> getFilterModel() {
        return (IModel<Filter>) getDefaultModel();
    }

    public boolean isNameFilterEnabled() {
        return nameFilterEnabled;
    }

    public void setNameFilterEnabled(boolean nameFilterEnabled) {
        this.nameFilterEnabled = nameFilterEnabled;
    }

    public boolean isBudgetFilterEnabled() {
        return budgetFilterEnabled;
    }

    public void setBudgetFilterEnabled(boolean budgetFilterEnabled) {
        this.budgetFilterEnabled = budgetFilterEnabled;
    }

    public boolean isDaterangeFilterEnabled() {
        return daterangeFilterEnabled;
    }

    public void setDaterangeFilterEnabled(boolean daterangeFilterEnabled) {
        this.daterangeFilterEnabled = daterangeFilterEnabled;
    }
}
