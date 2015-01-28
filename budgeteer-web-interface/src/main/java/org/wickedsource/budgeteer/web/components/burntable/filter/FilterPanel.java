package org.wickedsource.budgeteer.web.components.burntable.filter;

import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.person.PersonBaseData;
import org.wickedsource.budgeteer.service.person.PersonService;
import org.wickedsource.budgeteer.service.record.WorkRecordFilter;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.budget.BudgetBaseDataChoiceRenderer;
import org.wickedsource.budgeteer.web.components.daterange.DateRangeInputField;
import org.wickedsource.budgeteer.web.components.multiselect.MultiselectBehavior;
import org.wickedsource.budgeteer.web.components.person.PersonBaseDataChoiceRenderer;
import org.wicketstuff.lazymodel.LazyModel;

import java.util.HashMap;
import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class FilterPanel extends Panel {

    private boolean personFilterEnabled = true;

    private boolean budgetFilterEnabled = true;

    private boolean daterangeFilterEnabled = true;

    @SpringBean
    private PersonService personService;

    @SpringBean
    private BudgetService budgetService;

    @SuppressWarnings("unchecked")
    public FilterPanel(String id, WorkRecordFilter filter) {
        super(id, model(from(filter)));
        IModel<WorkRecordFilter> model = (IModel<WorkRecordFilter>) getDefaultModel();
        Form<WorkRecordFilter> form = new Form<WorkRecordFilter>("filterForm", model) {
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

    private WebMarkupContainer createPersonFilter(String id, Form<WorkRecordFilter> form) {
        WebMarkupContainer container = new WebMarkupContainer(id) {
            @Override
            public boolean isVisible() {
                return isPersonFilterEnabled();
            }
        };
        container.setVisible(isPersonFilterEnabled());
        LazyModel<List<PersonBaseData>> chosenPersons = model(from(form.getModelObject().getPersonList()));
        List<PersonBaseData> possiblePersons = personService.loadPeopleBaseData(BudgeteerSession.get().getProjectId());
        ListMultipleChoice<PersonBaseData> selectedPersons =
                new ListMultipleChoice<PersonBaseData>("personSelect", chosenPersons,
                        possiblePersons, new PersonBaseDataChoiceRenderer());


        selectedPersons.setRequired(false);
        HashMap<String, String> options = MultiselectBehavior.getRecommendedOptions();
        options.put("buttonWidth","'250px'");
        options.remove("buttonClass");
        selectedPersons.add(new MultiselectBehavior(options));
        container.add(selectedPersons);
        return container;
    }

    private WebMarkupContainer createBudgetFilter(String id, Form<WorkRecordFilter> form) {
        WebMarkupContainer container = new WebMarkupContainer(id) {
            @Override
            public boolean isVisible() {
                return isBudgetFilterEnabled();
            }
        };
        container.setVisible(isBudgetFilterEnabled());

        List<BudgetBaseData> possibleBudgets = budgetService.loadBudgetBaseDataForProject(BudgeteerSession.get().getProjectId());
        LazyModel<List<BudgetBaseData>> chosenBudgets = model(from(form.getModelObject().getBudgetList()));
        ListMultipleChoice<BudgetBaseData> selectedBudgets = new ListMultipleChoice<BudgetBaseData>("budgetSelect", chosenBudgets, possibleBudgets, new BudgetBaseDataChoiceRenderer());

        HashMap<String, String> options = MultiselectBehavior.getRecommendedOptions();
        options.put("buttonWidth","'250px'");
        options.remove("buttonClass");
        selectedBudgets.add(new MultiselectBehavior(options));
        selectedBudgets.setRequired(false);
        container.add(selectedBudgets);
        return container;
    }

    private WebMarkupContainer createDaterangeFilter(String id, Form<WorkRecordFilter> form) {
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
