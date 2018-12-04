package org.wickedsource.budgeteer.web.pages.budgets.fixedDailyRates.edit.form;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.dao.DataIntegrityViolationException;

import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRate;
import org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRateService;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.components.daterange.DateInputField;
import org.wickedsource.budgeteer.web.components.daterange.DateRangeInputField;
import org.wickedsource.budgeteer.web.components.money.MoneyTextField;
import org.wickedsource.budgeteer.web.pages.budgets.fixedDailyRates.FixedDailyRatesPage;
import org.wickedsource.budgeteer.web.pages.budgets.fixedDailyRates.edit.EditFixedDailyRatesPage;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class EditFixedDailyRateForm extends Form<FixedDailyRate> {
    @SpringBean
    private FixedDailyRateService service;
    private PageParameters backlinkParameters;
    private boolean isEditing;

    /**
     * Constructor for adding a new manual record
     */
    public EditFixedDailyRateForm(String id, PageParameters backlinkParameters) {
        super(id, new ClassAwareWrappingModel<>(Model.of(new FixedDailyRate(backlinkParameters.get("id").toLong())), FixedDailyRate.class));
        long ID = backlinkParameters.get("id").toLong();
        this.backlinkParameters = backlinkParameters;
        isEditing = false;
        addComponents();
    }

    /**
     * Constructor for editing an existing manual record
     */
    public EditFixedDailyRateForm(String id, IModel<FixedDailyRate> model, PageParameters backlinkParameters, boolean isEditingNewRecord, boolean isSaveResponse) {
        super(id, model);
        this.backlinkParameters = backlinkParameters;
        this.isEditing = true;
        Injector.get().inject(this);
        addComponents();
        if (isEditingNewRecord) {
            this.success("Record successfully created.");
        }
        if (isSaveResponse) {
            this.success("Changes successfully saved.");
        }
    }

    private void addComponents() {
        add(new CustomFeedbackPanel("feedback"));

        add(new RequiredTextField<>("title", model(from(getModel()).getName())));
        add(new TextField<>("description", model(from(getModel()).getDescription())));

        DateRangeInputField dateRangeField = new DateRangeInputField("dateRangeField", model(from(getModel()).getDateRange()), DateRangeInputField.DROP_LOCATION.UP);
        dateRangeField.setRequired(true);
        add(dateRangeField);

        MoneyTextField totalField = new MoneyTextField("total", model(from(getModel()).getMoneyAmount()));
        totalField.setRequired(true);
        add(totalField);

        //Label for the submit button
        Label submitButtonLabel;
        if (isEditing) {
            submitButtonLabel = new Label("submitButtonLabel", new ResourceModel("button.save.editmode"));
        } else {
            submitButtonLabel = new Label("submitButtonLabel", new ResourceModel("button.save.createmode"));
        }
        add(submitButtonLabel);
    }

    @Override
    protected void onSubmit() {
        try {
            if (!isEditing) {
                FixedDailyRate rate = getModelObject();
                isEditing = true;
                long newId = service.saveFixedDailyRate(getModelObject());
                setResponsePage(new EditFixedDailyRatesPage(EditFixedDailyRatesPage.createParameters(newId),
                        FixedDailyRatesPage.class, backlinkParameters, true, false));
            } else {
                this.success(getString("feedback.success"));
                FixedDailyRate rate = getModelObject();
                long newId = service.saveFixedDailyRate(getModelObject());
                setResponsePage(new EditFixedDailyRatesPage(EditFixedDailyRatesPage.createParameters(newId),
                        FixedDailyRatesPage.class, backlinkParameters, false, true));
            }
        } catch (DataIntegrityViolationException e) {
            this.error(getString("feedback.error.constraint"));
        }
    }
}