package org.wickedsource.budgeteer.web.pages.contract.overview.report.form;

import static org.apache.wicket.model.LambdaModel.of;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.wickedsource.budgeteer.service.ReportType;
import org.wickedsource.budgeteer.service.contract.ContractService;
import org.wickedsource.budgeteer.service.contract.report.ContractReportService;
import org.wickedsource.budgeteer.service.template.Template;
import org.wickedsource.budgeteer.service.template.TemplateService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.components.notificationlist.NotificationListPanel;
import org.wickedsource.budgeteer.web.pages.base.AbstractChoiceRenderer;
import org.wickedsource.budgeteer.web.pages.budgets.overview.report.form.BudgetReportNotificationModel;
import org.wickedsource.budgeteer.web.pages.contract.overview.report.ContractReportMetaInformation;

public class ContractReportForm extends Form<ContractReportMetaInformation> {

  /** */
  private static final long serialVersionUID = 1L;

  @SpringBean private ContractService contractService;

  @SpringBean private ContractReportService contractReportService;

  @SpringBean private TemplateService templateService;

  private List<FormattedDate> formattedMonths;

  public ContractReportForm(String id) {
    super(id, Model.of(new ContractReportMetaInformation()));
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
            of(
                getModel(),
                ContractReportMetaInformation::getSelectedMonth,
                ContractReportMetaInformation::setSelectedMonth),
            formattedMonths,
            choiceRenderer) {};
    add(choice);

    DropDownChoice<Template> templateDropDown =
        new DropDownChoice<Template>(
            "template",
            of(
                getModel(),
                ContractReportMetaInformation::getTemplate,
                ContractReportMetaInformation::setTemplate),
            new LoadableDetachableModel<List<? extends Template>>() {
              @Override
              protected List<? extends Template> load() {
                List<Template> temp = new ArrayList<>();
                Template defTemp =
                    templateService.getDefault(
                        ReportType.CONTRACT_REPORT, BudgeteerSession.get().getProjectId());
                if (defTemp != null) {
                  temp.add(defTemp);
                }
                for (Template e :
                    templateService.getTemplatesInProject(BudgeteerSession.get().getProjectId())) {
                  if (e.getType() == ReportType.CONTRACT_REPORT) {
                    if (defTemp == null) {
                      temp.add(e);
                    } else if (defTemp.getId() != e.getId()) {
                      temp.add(e);
                    }
                  }
                }
                if (temp.isEmpty()) {
                  temp.add(null);
                }
                return temp;
              }
            },
            new AbstractChoiceRenderer<Template>() {
              @Override
              public Object getDisplayValue(Template object) {
                String isDefault = "";
                if (object != null && object.isDefault()) {
                  isDefault = " (default)";
                }
                return object == null ? "No Templates Available" : object.getName() + isDefault;
              }
            }) {
          @Override
          public String getModelValue() {
            return null;
          }
        };
    templateDropDown.setNullValid(false);
    add(templateDropDown);

    add(new NotificationListPanel("notificationList", new BudgetReportNotificationModel()));
    add(new CustomFeedbackPanel("feedback"));
  }

  private List<FormattedDate> getFormatedMonths(List<Date> months, SimpleDateFormat formatter) {
    return months.stream()
        .map(month -> new FormattedDate(month, formatter))
        .collect(Collectors.toList());
  }

  protected void onSubmit() {
    String filename = "contract-report.xlsx";
    if ((getModelObject()).getTemplate() == null) {
      this.error(getString("feedback.error.no.template"));
    } else {
      this.success(getString("feedback.success"));
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
}
