package org.wickedsource.budgeteer.web.component.hourstable;

import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.people.PersonBaseData;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.component.choicerenderer.BudgetBaseDataChoiceRenderer;
import org.wickedsource.budgeteer.web.component.choicerenderer.PersonBaseDataChoiceRenderer;
import org.wickedsource.budgeteer.web.usecase.base.component.daterange.DateRangeInputField;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class FilterPanel extends Panel {

    private boolean personFilterEnabled = true;

    private boolean budgetFilterEnabled = true;

    private boolean daterangeFilterEnabled = true;

    public FilterPanel(String id) {
        super(id, model(from(new Filter())));
        Form<Filter> form = new Form<Filter>("filterForm", model(from(new Filter()))) {
            @Override
            protected void onSubmit() {
                send(getPage(), Broadcast.BREADTH, getModel().getObject());
            }
        };
        form.add(createPersonFilter("personFilterContainer", form));
        form.add(createBudgetFilter("budgetFilterContainer", form));
        form.add(createDaterangeFilter("daterangeFilterContainer", form));
        add(form);
    }

    private WebMarkupContainer createPersonFilter(String id, Form<Filter> form) {
        WebMarkupContainer container = new WebMarkupContainer(id) {
            @Override
            public boolean isVisible() {
                return isPersonFilterEnabled();
            }
        };
        container.setVisible(isPersonFilterEnabled());
        DropDownChoice<PersonBaseData> select = new DropDownChoice<PersonBaseData>("personSelect", model(from(form.getModel()).getSelectedPerson()), new PersonListModel(BudgeteerSession.get().getLoggedInUserId()));
        select.setChoiceRenderer(new PersonBaseDataChoiceRenderer());
        container.add(select);
        return container;
    }

    private WebMarkupContainer createBudgetFilter(String id, Form<Filter> form) {
        WebMarkupContainer container = new WebMarkupContainer(id) {
            @Override
            public boolean isVisible() {
                return isBudgetFilterEnabled();
            }
        };
        container.setVisible(isBudgetFilterEnabled());
        DropDownChoice<BudgetBaseData> select = new DropDownChoice<BudgetBaseData>("budgetSelect", model(from(form.getModel()).getSelectedBudget()), new BudgetListModel(BudgeteerSession.get().getLoggedInUserId()));
        select.setChoiceRenderer(new BudgetBaseDataChoiceRenderer());
        container.add(select);
        return container;
    }

    private WebMarkupContainer createDaterangeFilter(String id, Form<Filter> form) {
        WebMarkupContainer container = new WebMarkupContainer(id) {
            @Override
            public boolean isVisible() {
                return isDaterangeFilterEnabled();
            }
        };
        DateRangeInputField field = new DateRangeInputField("daterangeInput", model(from(form.getModel()).getSelectedDaterange()));
        container.add(field);
        return container;
    }

    public boolean isPersonFilterEnabled() {
        return personFilterEnabled;
    }

    public void setPersonFilterEnabled(boolean personFilterEnabled) {
        this.personFilterEnabled = personFilterEnabled;
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
