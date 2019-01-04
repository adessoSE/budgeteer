package de.adesso.budgeteer.web.pages.budgets.manualRecords.add.form;

import de.adesso.budgeteer.service.manualRecord.ManualRecord;
import de.adesso.budgeteer.service.manualRecord.ManualRecordService;
import de.adesso.budgeteer.web.ClassAwareWrappingModel;
import de.adesso.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import de.adesso.budgeteer.web.components.daterange.DateInputField;
import de.adesso.budgeteer.web.components.money.MoneyTextField;
import de.adesso.budgeteer.web.pages.budgets.manualRecords.add.AddManualRecordPage;
import de.adesso.budgeteer.web.pages.budgets.manualRecords.overview.ManualRecordOverviewPage;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Date;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class AddManualRecordForm extends Form<ManualRecord> {

    @SpringBean
    private ManualRecordService service;
    private PageParameters backlinkParameters;
    private boolean isEditing;

    /**
     * Constructor for adding a new manual record
     */
    public AddManualRecordForm(String id, PageParameters backlinkParameters) {
        super(id, new ClassAwareWrappingModel<>(Model.of(new ManualRecord(backlinkParameters.get("id").toLong())), ManualRecord.class));
        this.backlinkParameters = backlinkParameters;
        isEditing = false;
        addComponents();
    }

    /**
     * Constructor for editing an existing manual record
     */
    public AddManualRecordForm(String id, IModel<ManualRecord> model, PageParameters backlinkParameters, boolean isEditingNewRecord, boolean isSaveResponse) {
        super(id, model);
        this.backlinkParameters = backlinkParameters;
        this.isEditing = true;
        Injector.get().inject(this);
        addComponents();

        if(isEditingNewRecord)
        {
            this.success("Record successfully created.");
        }
        if(isSaveResponse){
            this.success("Changes successfully saved.");
        }
    }

    private void addComponents() {
        add(new CustomFeedbackPanel("feedback"));
        add(new RequiredTextField<>("description", model(from(getModel()).getDescription())));

        if(getModelObject().getBillingDate() == null){
            getModelObject().setBillingDate(new Date());
        }
        add(new DateInputField("billingDate", model(from(getModelObject()).getBillingDate())));

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
                isEditing = true;
                long newId = service.saveManualRecord(getModelObject());
                setResponsePage(new AddManualRecordPage(AddManualRecordPage.createParameters(newId),
                        ManualRecordOverviewPage.class, backlinkParameters, true, false));
            }
            else{
                this.success(getString("feedback.success"));
                long newId = service.saveManualRecord(getModelObject());
                setResponsePage(new AddManualRecordPage(AddManualRecordPage.createParameters(newId),
                        ManualRecordOverviewPage.class, backlinkParameters, false, true));
            }

        } catch (DataIntegrityViolationException e) {
            this.error(getString("feedback.error.constraint"));
        }
    }
}
