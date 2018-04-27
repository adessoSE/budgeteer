package org.wickedsource.budgeteer.web.pages.budgets.overview.report.form;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.springframework.dao.DataIntegrityViolationException;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.budget.report.BudgetReportService;
import org.wickedsource.budgeteer.service.budget.report.ReportMetaInformation;
import org.wickedsource.budgeteer.service.contract.ContractBaseData;
import org.wickedsource.budgeteer.service.template.Template;
import org.wickedsource.budgeteer.service.template.TemplateService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.components.daterange.DateRangeInputField;
import org.wickedsource.budgeteer.web.components.notificationlist.NotificationListPanel;
import org.wickedsource.budgeteer.web.pages.base.AbstractChoiceRenderer;
import org.wickedsource.budgeteer.web.pages.budgets.overview.report.BudgetReportFileModel;

import javax.inject.Inject;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

import java.io.File;
import java.util.Date;

public class BudgetReportForm extends Form<ReportMetaInformation> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SpringBean
	private BudgetReportService service;

    @Inject
    private TemplateService templateService;

	public BudgetReportForm(String id) {
		super(id, model(from(new ReportMetaInformation())));
		Date startDate = service.getStartDateOfBudgets();
		Date endDate = service.getLastDayOfLastMonth();
		Date monthlyStartDate = service.getFirstDayOfLastMonth();
		getModel().getObject().setOverallTimeRange(new DateRange(startDate, endDate));
		getModel().getObject().setMonthlyTimeRange(new DateRange(monthlyStartDate, endDate));

		add(new NotificationListPanel("notificationList", new BudgetReportNotificationModel()));
		add(new CustomFeedbackPanel("feedback"));
		add(new DateRangeInputField("monthlyRange", model(from(getModel()).getMonthlyTimeRange()),
				DateRangeInputField.DROP_LOCATION.DOWN));
		add(new DateRangeInputField("overallRange", model(from(getModel()).getOverallTimeRange()),
				DateRangeInputField.DROP_LOCATION.DOWN));
        DropDownChoice<Template> templateDropDown = new DropDownChoice<Template>("template", model(from(getModel()).getTemplate()),
                templateService.getTemplates(),
                new AbstractChoiceRenderer<Template>() {
                    @Override
                    public Object getDisplayValue(Template object) {
                        return object == null ? "Unnamed" : object.getName();
                    }
                });
        templateDropDown.setNullValid(false);
        add(templateDropDown);
	}

	protected void onSubmit() {
		String filename = "report.xlsx";
        if(((ReportMetaInformation) getModelObject()).getTemplate() == null){
            this.error(getString("feedback.error.no.template"));
        }else {
            this.success(getString("feedback.success"));
            IModel<File> fileModel = new BudgetReportFileModel(BudgeteerSession.get().getProjectId(),
                    model(from(BudgeteerSession.get().getBudgetFilter())), getModel(), model(from(getModel()).getTemplate()));

            final File file = fileModel.getObject();
            IResourceStream resourceStream = new FileResourceStream(file);
            getRequestCycle().scheduleRequestHandlerAfterCurrent(new ResourceStreamRequestHandler(resourceStream) {
                @Override
                public void respond(IRequestCycle requestCycle) {
                    super.respond(requestCycle);
                    Files.remove(file);
                }
            }.setFileName(filename).setContentDisposition(ContentDisposition.ATTACHMENT));
        }



	}

}
