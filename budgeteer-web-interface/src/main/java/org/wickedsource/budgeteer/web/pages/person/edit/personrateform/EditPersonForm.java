package org.wickedsource.budgeteer.web.pages.person.edit.personrateform;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.DateUtil;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.service.person.PersonRate;
import org.wickedsource.budgeteer.service.person.PersonService;
import org.wickedsource.budgeteer.service.person.PersonWithRates;
import org.wickedsource.budgeteer.web.components.dataTable.DataTableBehavior;
import org.wickedsource.budgeteer.web.pages.person.edit.EditPersonPage;
import org.wickedsource.budgeteer.web.pages.person.edit.IEditPersonPageStrategy;

import java.util.*;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class EditPersonForm extends Form<PersonWithRates> {

    @SpringBean
    private PersonService peopleService;

    private IEditPersonPageStrategy strategy;

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
        WebMarkupContainer table = new WebMarkupContainer("ratesTable");
        HashMap<String, String> options = DataTableBehavior.getRecommendedOptions();
        options.put("info", "false");
        options.put("paging", "false");
        table.add(new DataTableBehavior(options));

        add(new RequiredTextField<>("name", model(from(getModel()).getName())));
        add(new RequiredTextField<>("importKey", model(from(getModel()).getImportKey())));
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

        AjaxButton submitButton = new AjaxButton("submitButton"){
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                PersonWithRates model = (PersonWithRates)form.getModelObject();
                peopleService.savePersonWithRates(model);
                ((EditPersonPage) getPage()).goBack();
            }
        };
        submitButton.setDefaultFormProcessing(false);
        submitButton.add(strategy.createSubmitButtonLabel("submitButtonTitle"));
        add(submitButton);
        add(table);
    }

    private ListView<PersonRate> createRatesList(String id) {
        return new ListView<PersonRate>(id, model(from(getModel()).getRates())) {

            private String infoOrEditPanel = "infoOrEditPanel";

            @Override
            protected void populateItem(final ListItem<PersonRate> item) {
                item.setOutputMarkupId(true);
                item.add(new PersonInfoPanel(infoOrEditPanel, item.getModelObject(),EditPersonForm.this.getModelObject().getRates()){

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
