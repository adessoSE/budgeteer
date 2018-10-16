package org.wickedsource.budgeteer.web.pages.budgets.manualRecords.form;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.wickedsource.budgeteer.persistence.record.AddManualRecordData;
import org.wickedsource.budgeteer.service.contract.ContractService;
import org.wickedsource.budgeteer.service.record.RecordService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.components.money.MoneyTextField;
import org.wickedsource.budgeteer.web.components.notificationlist.NotificationListPanel;
import org.wickedsource.budgeteer.web.pages.budgets.details.BudgetDetailsPage;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class AddManualRecordForm extends Form<AddManualRecordData> {

    @SpringBean
    private RecordService service;
    private PageParameters backlinkParameters;

    @SpringBean
    private ContractService contractService;

    public AddManualRecordForm(String id,PageParameters backlinkParameters) {
        super(id, new ClassAwareWrappingModel<>(Model.of(new AddManualRecordData(BudgeteerSession.get().getProjectId())), AddManualRecordData.class));
        this.backlinkParameters = backlinkParameters;
        addComponents();
    }

    public AddManualRecordForm(String id, IModel<AddManualRecordData> model, PageParameters backlinkParameters) {
        super(id, model);
        Injector.get().inject(this);
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
            // ToDo
            service.saveManualRecord(getModelObject());
            setResponsePage(new BudgetDetailsPage(backlinkParameters));

        } catch (DataIntegrityViolationException e) {
            this.error(getString("feedback.error.constraint"));
        }
    }
}
