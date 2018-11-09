package org.wickedsource.budgeteer.web.pages.budgets.manualRecords.add.form;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.wickedsource.budgeteer.persistence.manualRecord.ManualRecord;
import org.wickedsource.budgeteer.service.manualRecord.ManualRecordService;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.components.money.MoneyTextField;
import org.wickedsource.budgeteer.web.components.notificationlist.NotificationListPanel;
import org.wickedsource.budgeteer.web.pages.budgets.manualRecords.overview.ManualRecordOverviewPage;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class AddManualRecordForm extends Form<ManualRecord> {

    @SpringBean
    private ManualRecordService service;
    private PageParameters backlinkParameters;

    public AddManualRecordForm(String id, PageParameters backlinkParameters) {
        super(id, new ClassAwareWrappingModel<>(Model.of(new ManualRecord(backlinkParameters.get("id").toLong())), ManualRecord.class));
        this.backlinkParameters = backlinkParameters;
        addComponents();
    }

    private void addComponents() {
        add(new CustomFeedbackPanel("feedback"));
        add(new RequiredTextField<>("description", model(from(getModel()).getDescription())));

        MoneyTextField totalField = new MoneyTextField("total", model(from(getModel()).getMoneyAmount()));
        totalField.setRequired(true);
        add(totalField);

        add(new NotificationListPanel("notificationList", new ManualRecordNotificationsModel(getModel().getObject().getId())));
    }

    @Override
    protected void onSubmit() {
        try {
            service.saveManualRecord(getModelObject());
            setResponsePage(new ManualRecordOverviewPage(backlinkParameters));
        } catch (DataIntegrityViolationException e) {
            this.error(getString("feedback.error.constraint"));
        }
    }
}
