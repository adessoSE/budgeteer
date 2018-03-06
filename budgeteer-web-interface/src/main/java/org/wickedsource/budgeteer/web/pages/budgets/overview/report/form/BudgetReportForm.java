package org.wickedsource.budgeteer.web.pages.budgets.overview.report.form;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.report.BudgetReportMetaInformation;
import org.wickedsource.budgeteer.service.report.ReportService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.components.daterange.DateRangeInputField;
import org.wickedsource.budgeteer.web.components.notificationlist.NotificationListPanel;
import org.wickedsource.budgeteer.web.pages.budgets.overview.report.BudgetReportModel;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

import java.io.File;
import java.util.Date;

public class BudgetReportForm extends Form<BudgetReportMetaInformation> {

	@SpringBean
	private ReportService service;
	
	public BudgetReportForm(String id) {
		super(id, model(from(new BudgetReportMetaInformation())));
		Date startDate = service.getStartDateOfBudgets();
		Date endDate = service.getFirstOfTheMonth();
		getModel().getObject().setDateRange(new DateRange(startDate, endDate));

		
        add(new NotificationListPanel("notificationList", new BudgetReportNotificationModel()));
        add(new CustomFeedbackPanel("feedback"));
        add(new DateRangeInputField("dateRange", model(from(getModel()).getDateRange()), DateRangeInputField.DROP_LOCATION.DOWN));
	}
	
    protected void onSubmit() {
        this.success(getString("feedback.success"));
    	IModel<File> fileModel = new BudgetReportModel(BudgeteerSession.get().getProjectId(),
				model(from(BudgeteerSession.get().getBudgetFilter())), getModel());
    	final File file = fileModel.getObject();
        IResourceStream resourceStream = new FileResourceStream(file);
        String filename = "report.xlsx";
		getRequestCycle().scheduleRequestHandlerAfterCurrent(new ResourceStreamRequestHandler(resourceStream) {
        	@Override
        	public void respond(IRequestCycle requestCycle) {
        		super.respond(requestCycle);
        		Files.remove(file);
        	}
        }.setFileName(filename)
				.setContentDisposition(ContentDisposition.ATTACHMENT)
				);
}

}
