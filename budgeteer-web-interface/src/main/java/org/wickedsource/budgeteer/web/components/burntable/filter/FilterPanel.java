package org.wickedsource.budgeteer.web.components.burntable.filter;

import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.people.PersonBaseData;
import org.wickedsource.budgeteer.service.record.RecordFilter;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.budget.BudgetBaseDataChoiceRenderer;
import org.wickedsource.budgeteer.web.components.daterange.DateRangeInputField;
import org.wickedsource.budgeteer.web.components.person.PersonBaseDataChoiceRenderer;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class FilterPanel extends Panel {

    private boolean personFilterEnabled = true;

    private boolean budgetFilterEnabled = true;

    private boolean daterangeFilterEnabled = true;

    @SuppressWarnings("unchecked")
    public FilterPanel(String id, RecordFilter filter) {
        super(id, model(from(filter)));
        IModel<RecordFilter> model = (IModel<RecordFilter>) getDefaultModel();
        Form<RecordFilter> form = new Form<RecordFilter>("filterForm", model) {
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

    private WebMarkupContainer createPersonFilter(String id, Form<RecordFilter> form) {
        WebMarkupContainer container = new WebMarkupContainer(id) {
            @Override
            public boolean isVisible() {
                return isPersonFilterEnabled();
            }
        };
        container.setVisible(isPersonFilterEnabled());
        DropDownChoice<PersonBaseData> field = new DropDownChoice<PersonBaseData>("personSelect", model(from(form.getModel()).getPerson()), new PersonListModel(BudgeteerSession.get().getLoggedInUserId()));
        field.setChoiceRenderer(new PersonBaseDataChoiceRenderer());
        field.setRequired(false);
        field.setNullValid(true);
        container.add(field);
        return container;
    }

    private WebMarkupContainer createBudgetFilter(String id, Form<RecordFilter> form) {
        WebMarkupContainer container = new WebMarkupContainer(id) {
            @Override
            public boolean isVisible() {
                return isBudgetFilterEnabled();
            }
        };
        container.setVisible(isBudgetFilterEnabled());
        DropDownChoice<BudgetBaseData> field = new DropDownChoice<BudgetBaseData>("budgetSelect", model(from(form.getModel()).getBudget()), new BudgetListModel(BudgeteerSession.get().getLoggedInUserId()));
        field.setChoiceRenderer(new BudgetBaseDataChoiceRenderer());
        field.setRequired(false);
        field.setNullValid(true);
        container.add(field);
        return container;
    }

    private WebMarkupContainer createDaterangeFilter(String id, Form<RecordFilter> form) {
        WebMarkupContainer container = new WebMarkupContainer(id) {
            @Override
            public boolean isVisible() {
                return isDaterangeFilterEnabled();
            }
        };
        DateRangeInputField field = new DateRangeInputField("daterangeInput", model(from(form.getModel()).getDateRange()));
        field.setRequired(false);
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
