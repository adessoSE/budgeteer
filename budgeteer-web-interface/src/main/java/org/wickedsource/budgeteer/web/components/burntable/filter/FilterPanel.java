package org.wickedsource.budgeteer.web.components.burntable.filter;

import static org.apache.wicket.model.LambdaModel.of;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.wicket.Page;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.person.PersonBaseData;
import org.wickedsource.budgeteer.service.person.PersonService;
import org.wickedsource.budgeteer.service.record.WorkRecordFilter;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.budget.BudgetBaseDataChoiceRenderer;
import org.wickedsource.budgeteer.web.components.daterange.DateRangeInputField;
import org.wickedsource.budgeteer.web.components.links.ResetFilterLink;
import org.wickedsource.budgeteer.web.components.multiselect.MultiselectBehavior;
import org.wickedsource.budgeteer.web.components.person.PersonBaseDataChoiceRenderer;

public class FilterPanel extends Panel {

  private boolean personFilterEnabled = true;

  private boolean budgetFilterEnabled = true;

  private boolean daterangeFilterEnabled = true;

  private boolean sortingFilterEnabled = true;

  @SpringBean private PersonService personService;

  @SpringBean private BudgetService budgetService;

  @SuppressWarnings("unchecked")
  public FilterPanel(String id, WorkRecordFilter filter, Page page, PageParameters pageParameters) {
    super(id, Model.of(filter));
    IModel<WorkRecordFilter> model = (IModel<WorkRecordFilter>) getDefaultModel();
    Form<WorkRecordFilter> form =
        new Form<WorkRecordFilter>("filterForm", model) {
          @Override
          protected void onSubmit() {
            send(getPage(), Broadcast.BREADTH, getModel().getObject());
          }
        };
    form.add(createPersonFilter("personFilterContainer", form));
    form.add(createBudgetFilter("budgetFilterContainer", form));
    form.add(createDaterangeFilter("daterangeFilterContainer", form));
    form.add(createSortingFilter("sortingFilterContainer", form));
    form.add(new ResetFilterLink("resetButton", filter, page, pageParameters));
    add(form);
  }

  private WebMarkupContainer createSortingFilter(String id, Form<WorkRecordFilter> form) {
    WebMarkupContainer container =
        new WebMarkupContainer(id) {
          @Override
          public boolean isVisible() {
            return isSortingFilterEnabled();
          }
        };
    container.setVisible(isSortingFilterEnabled());
    DropDownChoice<BurnTableSortColumn> columnSelect =
        new DropDownChoice<>(
            "columnSelect",
            form.getModelObject().getColumnToSort(),
            Arrays.asList(BurnTableSortColumn.values()));

    List<String> possibleSortTypes = Arrays.asList("Ascending", "Descending");
    DropDownChoice<String> sortTypeSelect =
        new DropDownChoice<>(
            "sortTypeSelect", form.getModelObject().getSortType(), possibleSortTypes);

    HashMap<String, String> options = MultiselectBehavior.getRecommendedOptions();
    options.put("buttonWidth", "'120px'");
    options.remove("buttonClass");
    options.put("enableCaseInsensitiveFiltering", "false");
    options.put("enableFiltering", "false");

    columnSelect.add(new MultiselectBehavior(options));
    columnSelect.setRequired(false);

    sortTypeSelect.add(new MultiselectBehavior(options));
    sortTypeSelect.setRequired(false);

    container.add(columnSelect);
    container.add(sortTypeSelect);

    return container;
  }

  private WebMarkupContainer createPersonFilter(String id, Form<WorkRecordFilter> form) {
    WebMarkupContainer container =
        new WebMarkupContainer(id) {
          @Override
          public boolean isVisible() {
            return isPersonFilterEnabled();
          }
        };
    container.setVisible(isPersonFilterEnabled());
    IModel<List<PersonBaseData>> chosenPersons =
        form.getModel().map(WorkRecordFilter::getPersonList);
    List<PersonBaseData> possiblePersonsFromFilter = form.getModelObject().getPossiblePersons();
    List<PersonBaseData> possiblePersons =
        possiblePersonsFromFilter.isEmpty()
            ? personService.loadPeopleBaseData(BudgeteerSession.get().getProjectId())
            : possiblePersonsFromFilter;
    ListMultipleChoice<PersonBaseData> selectedPersons =
        new ListMultipleChoice<>(
            "personSelect", chosenPersons, possiblePersons, new PersonBaseDataChoiceRenderer());

    selectedPersons.setRequired(false);
    HashMap<String, String> options = MultiselectBehavior.getRecommendedOptions();
    options.put("buttonWidth", "'250px'");
    options.remove("buttonClass");
    selectedPersons.add(new MultiselectBehavior(options));
    container.add(selectedPersons);
    return container;
  }

  private WebMarkupContainer createBudgetFilter(String id, Form<WorkRecordFilter> form) {
    WebMarkupContainer container =
        new WebMarkupContainer(id) {
          @Override
          public boolean isVisible() {
            return isBudgetFilterEnabled();
          }
        };
    container.setVisible(isBudgetFilterEnabled());
    List<BudgetBaseData> possibleBudgetsFromFilter = form.getModelObject().getPossibleBudgets();
    List<BudgetBaseData> possibleBudgets =
        possibleBudgetsFromFilter.isEmpty()
            ? budgetService.loadBudgetBaseDataForProject(BudgeteerSession.get().getProjectId())
            : possibleBudgetsFromFilter;
    IModel<List<BudgetBaseData>> chosenBudgets =
        form.getModel().map(WorkRecordFilter::getBudgetList);
    ListMultipleChoice<BudgetBaseData> selectedBudgets =
        new ListMultipleChoice<>(
            "budgetSelect", chosenBudgets, possibleBudgets, new BudgetBaseDataChoiceRenderer());

    HashMap<String, String> options = MultiselectBehavior.getRecommendedOptions();
    options.put("buttonWidth", "'250px'");
    options.remove("buttonClass");
    selectedBudgets.add(new MultiselectBehavior(options));
    selectedBudgets.setRequired(false);
    container.add(selectedBudgets);
    return container;
  }

  private WebMarkupContainer createDaterangeFilter(String id, Form<WorkRecordFilter> form) {
    WebMarkupContainer container =
        new WebMarkupContainer(id) {
          @Override
          public boolean isVisible() {
            return isDaterangeFilterEnabled();
          }
        };
    DateRangeInputField field =
        new DateRangeInputField(
            "daterangeInput",
            of(form.getModel(), WorkRecordFilter::getDateRange, WorkRecordFilter::setDateRange));
    field.setRequired(false);
    container.add(field);
    return container;
  }

  private boolean isSortingFilterEnabled() {
    return sortingFilterEnabled;
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
