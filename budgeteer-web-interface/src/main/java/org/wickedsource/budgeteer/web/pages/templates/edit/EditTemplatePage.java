package org.wickedsource.budgeteer.web.pages.templates.edit;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.string.StringValue;
import org.wickedsource.budgeteer.imports.api.ImportFile;
import org.wickedsource.budgeteer.service.template.Template;
import org.wickedsource.budgeteer.service.template.TemplateService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.pages.templates.TemplatesPage;
import org.wickedsource.budgeteer.web.pages.templates.templateimport.TemplateFormInputDto;

/**
 * Creates the form to edit a template. (Reupload, Download, Delete, Edit name/description)
 *
 * @author maximAtanasov
 */
@Mount("templates/editTemplates/#{id}")
public class EditTemplatePage extends DialogPageWithBacklink {

  @SpringBean private TemplateService service;

  public EditTemplatePage(PageParameters parameters) {
    this(TemplatesPage.class, new PageParameters(), parameters);
  }

  /**
   * @param backlinkPage The page to go back to (Here, always the Template Overview page)
   * @param backlinkParameters The parameters to pass to that page.
   */
  public EditTemplatePage(
      Class<? extends WebPage> backlinkPage,
      PageParameters backlinkParameters,
      PageParameters parameters) {
    super(parameters, backlinkPage, backlinkParameters);
    add(createBacklink("backlink1"));

    long templateId = getTemplateId();
    if (templateId <= 0) {
      goBack();
    } else {
      var template = service.getById(getTemplateId());
      if (template == null) {
        setResponsePage(DashboardPage.class);
        return;
      }
      var model =
          Model.of(
              new TemplateFormInputDto(
                  template.getProjectID(),
                  template.getName(),
                  template.getDescription(),
                  template.getType(),
                  template.isDefault()));
      var form = new Form<>("form");
      form.add(new CustomFeedbackPanel("feedback"));
      form.add(createBacklink("backlink2"));
      form.add(deleteTemplateButton());
      form.add(new EditTemplateInputPanel("editTemplate", model));
      form.add(downloadFileButton());
      form.add(createSaveButton(model));
      add(form);
    }
  }

  private Link<Void> deleteTemplateButton() {
    return new Link<>("deleteButton") {
      @Override
      public void onClick() {
        setResponsePage(
            new DeleteDialog() {
              @Override
              protected void onYes() {
                service.deleteTemplate(getTemplateId());
                goBack();
              }

              @Override
              protected void onNo() {
                setResponsePage(
                    new EditTemplatePage(
                        TemplatesPage.class,
                        getPage().getPageParameters(),
                        BasePage.createParameters(getTemplateId())));
              }

              @Override
              protected String confirmationText() {
                return EditTemplatePage.this.getString("delete.template.confirmation");
              }
            });
      }
    };
  }

  private Link<Void> downloadFileButton() {
    return new Link<>("downloadFileButton") {
      @Override
      public void onClick() {
        Template template = service.getById(getTemplateId());
        XSSFWorkbook wb = template.getWb();
        AbstractResourceStreamWriter streamWriter =
            new AbstractResourceStreamWriter() {
              @Override
              public void write(OutputStream output) throws IOException {
                wb.write(output);
              }
            };
        ResourceStreamRequestHandler handler =
            new ResourceStreamRequestHandler(streamWriter, template.getName() + ".xlsx");
        getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
        HttpServletResponse response =
            (HttpServletResponse) getRequestCycle().getResponse().getContainerResponse();
        response.setContentType(null);
      }
    };
  }

  private Component createSaveButton(IModel<TemplateFormInputDto> model) {
    return new Button("save") {
      @Override
      public void onSubmit() {
        var modelObject = model.getObject();
        try {
          var importFile =
              modelObject.getFileUploads() == null || modelObject.getFileUploads().isEmpty()
                  ? null
                  : new ImportFile(
                      modelObject.getFileUploads().get(0).getClientFileName(),
                      modelObject.getFileUploads().get(0).getInputStream());
          service.editTemplate(
              BudgeteerSession.get().getProjectId(), getTemplateId(), importFile, model);
          success(getString("message.success"));
        } catch (IOException e) {
          error(String.format(getString("message.ioError"), e.getMessage()));
        } catch (IllegalArgumentException e) {
          error(String.format(getString("message.importError"), e.getMessage()));
        }
      }
    };
  }

  private long getTemplateId() {
    StringValue value = getPageParameters().get("id");
    if (value == null || value.isEmpty() || value.isNull()) {
      return 0L;
    } else {
      return value.toLong();
    }
  }
}
