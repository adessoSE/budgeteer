package org.wickedsource.budgeteer.web.pages.contract.overview.report.form;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.wickedsource.budgeteer.service.contract.ContractService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.components.notificationlist.NotificationListPanel;
import org.wickedsource.budgeteer.web.pages.budgets.overview.report.form.BudgetReportNotificationModel;
import org.wickedsource.budgeteer.web.pages.contract.overview.report.ContractReportMetaInformation;

public class ContractReportForm extends Form<ContractReportMetaInformation> {

	/** */
	private static final long serialVersionUID = 1L;

	@SpringBean private ContractService contractService;

	private List<FormattedDate> formattedMonths;

	public ContractReportForm(String id) {
		super(id, model(from(new ContractReportMetaInformation())));
		List<Date> months =
				contractService.getMonthListForProjectId(BudgeteerSession.get().getProjectId());

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy, MMMM");
		formattedMonths = getFormatedMonths(months, formatter);

		getModelObject().setSelectedMonth(formattedMonths.get(formattedMonths.size() - 1));

		IChoiceRenderer<FormattedDate> choiceRenderer =
				new IChoiceRenderer<FormattedDate>() {
					@Override
					public Object getDisplayValue(FormattedDate object) {
						return object.getLabel();
					}

					@Override
					public String getIdValue(FormattedDate object, int index) {
						return String.valueOf(formattedMonths.indexOf(object));
					}

					@Override
					public FormattedDate getObject(
							String id, IModel<? extends List<? extends FormattedDate>> choices) {
						getModelObject().setSelectedMonth(choices.getObject().get(Integer.parseInt(id)));
						return choices.getObject().get(Integer.parseInt(id));
					}
				};

		DropDownChoice<FormattedDate> choice =
				new DropDownChoice<FormattedDate>(
						"selectMonth",
						model(from(getModel()).getSelectedMonth()),
						formattedMonths,
						choiceRenderer) {};
		add(choice);

		add(new NotificationListPanel("notificationList", new BudgetReportNotificationModel()));
		add(new CustomFeedbackPanel("feedback"));
	}

	private List<FormattedDate> getFormatedMonths(List<Date> months, SimpleDateFormat formatter) {
		return months
				.stream()
				.map(month -> new FormattedDate(month, formatter))
				.collect(Collectors.toList());
	}

	protected void onSubmit() {
		String filename = "contract-report.xlsx";
		// this.success(getString("feedback.success"));
		IModel<File> fileModel =
				new ContractReportFileModel(BudgeteerSession.get().getProjectId(), getModel());
		final File file = fileModel.getObject();
		IResourceStream resourceStream = new FileResourceStream(file);
		getRequestCycle()
				.scheduleRequestHandlerAfterCurrent(
						new ResourceStreamRequestHandler(resourceStream) {
							@Override
							public void respond(IRequestCycle requestCycle) {
								super.respond(requestCycle);
								Files.remove(file);
							}
						}.setFileName(filename).setContentDisposition(ContentDisposition.ATTACHMENT));
	}
}
