package org.wickedsource.budgeteer.web.pages.person.edit.personrateform;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.DateUtil;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.notification.MissingDailyRateForBudgetNotification;
import org.wickedsource.budgeteer.service.notification.Notification;
import org.wickedsource.budgeteer.service.person.PersonRate;
import org.wickedsource.budgeteer.service.person.PersonService;
import org.wickedsource.budgeteer.service.person.PersonWithRates;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.components.dataTable.DataTableBehavior;
import org.wickedsource.budgeteer.web.components.listMultipleChoiceWithGroups.OptionGroup;
import org.wickedsource.budgeteer.web.pages.person.edit.IEditPersonPageStrategy;

import java.util.*;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class EditPersonForm extends Form<PersonWithRates> {

    @SpringBean
    private PersonService peopleService;

    @SpringBean
    private BudgetService budgetService;

    private IEditPersonPageStrategy strategy;

    private TextField nameTextField;
    private TextField importKeyTextField;

    /**
     * Use this constructor for a form that creates a new user.
     */
    public EditPersonForm(String id, IEditPersonPageStrategy strategy) {
        super(id, new PersonWithRatesModel(new PersonWithRates()));
        this.strategy = strategy;
        Injector.get().inject(this);
        addComponents();
    }

    /**
     * Use this constructor for a form that edits and existing user.
     */
    public EditPersonForm(String id, PersonWithRates person, IEditPersonPageStrategy strategy) {
        super(id, new PersonWithRatesModel(person));
        this.strategy = strategy;
        addComponents();
    }

    private void addComponents() {
        setOutputMarkupId(true);
        CustomFeedbackPanel feedbackPanel = new CustomFeedbackPanel("mainFormFeedback");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);
        WebMarkupContainer table = new WebMarkupContainer("ratesTable");
        HashMap<String, String> options = DataTableBehavior.getRecommendedOptions();
        options.put("info", Boolean.toString(false));
        options.put("paging", Boolean.toString(false));
        options.put("searching", Boolean.toString(false));
        options.put("order", "[]");
        table.add(new DataTableBehavior(options));
        nameTextField = new TextField<>("name", model(from(getModelObject()).getName()));
        importKeyTextField = new TextField<>("importKey", model(from(getModelObject()).getImportKey()));
        add(nameTextField, importKeyTextField);

        table.add(createRatesList("ratesList").setOutputMarkupId(true));
        PersonEditPanel personEditPanel = new PersonEditPanel("newRatePanel", new PersonRateFormDto(getModelObject().getPersonId(),
                new ArrayList<>(), Money.zero(CurrencyUnit.EUR), new DateRange(new Date(), new Date())), false) {

            @Override
            protected void rateAdded(PersonRate rate) {
                EditPersonForm.this.getModelObject().getRates().add(rate);
            }

            @Override
            protected List<PersonRate> getOverlappingRates(DateRange dateRange, BudgetBaseData budget) {
                List<PersonRate> result = new LinkedList<>();
                for(PersonRate p : EditPersonForm.this.getModelObject().getRates()){
                    if(p.getBudget().equals(budget) && DateUtil.isDateRangeOverlapping(p.getDateRange(), dateRange)){
                        result.add(p);
                    }
                }
                return result;
            }
        };

        table.add(personEditPanel);

        Button submitButton = new Button("submitButton"){
            @Override
            public void onSubmit() {
                if(importKeyTextField.getInput() == null || importKeyTextField.getInput().isEmpty()){
                    this.error(getString("form.missing.import.key"));
                }
                if(nameTextField.getInput() == null || nameTextField.getInput().isEmpty()){
                    this.error(getString("form.missing.name"));
                }
                if(!nameTextField.getInput().isEmpty() && !importKeyTextField.getInput().isEmpty()) {
                    EditPersonForm.this.getModelObject().setName(nameTextField.getInput());
                    EditPersonForm.this.getModelObject().setImportKey(importKeyTextField.getInput());
                    peopleService.savePersonWithRates(EditPersonForm.this.getModelObject());
                    this.success(getString("form.success"));
                }
            }
        };
        submitButton.setDefaultFormProcessing(false);
        submitButton.add(strategy.createSubmitButtonLabel("submitButtonTitle"));
        add(submitButton);
        add(table);
    }

    @Override
    public void onEvent(IEvent<?> event) {
        if(event.getPayload() instanceof Notification){
            MissingDailyRateForBudgetNotification notification = (MissingDailyRateForBudgetNotification)event.getPayload();
            DateRange dateRange = new DateRange(notification.getStartDate(), notification.getEndDate());
            String budgetName = notification.getBudgetName();
            BudgetBaseData budgetData;
            for(OptionGroup<BudgetBaseData> e : budgetService.getPossibleBudgetDataForPersonAndProject(BudgeteerSession.get().getProjectId(), getModelObject().getPersonId())){
                for(BudgetBaseData budget : e.getOptions()){
                    if(budget.getName().equals(budgetName)){
                        budgetData = budget;
                        ((PersonEditPanel)this.get("ratesTable").get("newRatePanel")).setData(new PersonRateFormDto(this.getModelObject().getPersonId(),
                                Collections.singletonList(budgetData), Money.zero(CurrencyUnit.EUR), dateRange));
                    }
                }
            }
            RequestCycle.get().find(AjaxRequestTarget.class).add(this);
        }
    }

    private ListView<PersonRate> createRatesList(String id) {
        return new ListView<PersonRate>(id, model(from(getModel()).getRates())) {

            private String infoOrEditPanel = "infoOrEditPanel";

            @Override
            protected void populateItem(final ListItem<PersonRate> item) {
                item.setOutputMarkupId(true);
                item.add(new PersonInfoPanel(infoOrEditPanel, item.getModelObject(), EditPersonForm.this.getModelObject().getRates()) {

                        @Override
                        protected ListItem<PersonRate> getEditPanel() {

                            PersonRateFormDto dto = new PersonRateFormDto(EditPersonForm.this.getModelObject().getPersonId(),
                                    Collections.singletonList(item.getModelObject().getBudget()),
                                    item.getModelObject().getRate(),
                                    item.getModelObject().getDateRange());
                            PersonEditPanel editPanel = createPanelToEditExistingPerson(infoOrEditPanel, dto, item.getModelObject());
                            editPanel.setOutputMarkupId(true);
                            item.remove(infoOrEditPanel);
                            item.add(editPanel);
                            return item;
                        }
                    }.setOutputMarkupId(true));
                }

            @Override
            protected ListItem<PersonRate> newItem(int index, IModel<PersonRate> itemModel) {
                // wrap into Model that can be used with LazyModel
                return super.newItem(index, new PersonRateModel(itemModel));
            }
        };
    }

    private PersonEditPanel createPanelToEditExistingPerson(String id, PersonRateFormDto dto, PersonRate previousRate){
        return new PersonEditPanel(id, dto, true) {

            boolean newRateIsAdded = false;

            @Override
            protected void rateAdded(PersonRate rate) {
                if (newRateIsAdded) {
                    getModelObject().getRates().add(rate);
                } else {
                    getModelObject().getRates().replaceAll(personRate -> {
                        if (personRate.equals(previousRate)) {
                            newRateIsAdded = true;
                            return rate;
                        } else {
                            return personRate;
                        }
                    });
                }
            }

            @Override
            protected List<PersonRate> getOverlappingRates(DateRange dateRange, BudgetBaseData budget) {
                List<PersonRate> result = new LinkedList<>();
                for (PersonRate p : getModelObject().getRates()) {
                    if (!p.equals(previousRate) && p.getBudget().equals(budget) && DateUtil.isDateRangeOverlapping(p.getDateRange(), dateRange)) {
                        result.add(p);
                    }
                }
                return result;
            }
        };
    }
}
