package org.wickedsource.budgeteer.web.pages.contract.edit;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.springframework.dao.DataIntegrityViolationException;
import org.wickedsource.budgeteer.service.contract.ContractBaseData;
import org.wickedsource.budgeteer.service.contract.ContractService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;
import org.wickedsource.budgeteer.web.pages.contract.edit.form.EditContractAdditionalAttributesPanel;
import org.wickedsource.budgeteer.web.pages.contract.edit.form.EditContractInputPanel;
import org.wickedsource.budgeteer.web.pages.contract.overview.ContractOverviewPage;

@Mount({"contracts/edit/#{id}"})
public class EditContractPage extends DialogPageWithBacklink {

  @SpringBean private ContractService service;

  /**
   * This constructor is used when you click on a link or try to access the EditContractPage
   * manually (e.g. when you type the path "/contracts/edit" in the search bar)
   *
   * @param parameters
   */
  public EditContractPage(PageParameters parameters) {
    this(parameters, ContractOverviewPage.class, new PageParameters());
  }

  /** Use this constructor to create a page with a form to create a new contract. */
  public EditContractPage(
      Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
    this(new PageParameters(), backlinkPage, backlinkParameters);
  }

  /**
   * Use this constructor to create a page with a form to edit an existing contract.
   *
   * @param parameters page parameters containing the id of the budget to edit.
   */
  public EditContractPage(
      PageParameters parameters,
      Class<? extends WebPage> backlinkPage,
      PageParameters backlinkParameters) {
    super(parameters, backlinkPage, backlinkParameters);
    var model =
        Model.of(
            getContractId() == 0
                ? service.getEmptyContractModel(BudgeteerSession.get().getProjectId())
                : service.getContractById(getContractId()));

    add(
        new Label(
            "pageTitle",
            () ->
                getString(getContractId() == 0 ? "page.title.createmode" : "page.title.editmode")));
    add(createBacklink("cancelButton1"));

    var form = new Form<>("form");
    var feedbackPanel = new CustomFeedbackPanel("feedback").setOutputMarkupId(true);
    form.add(feedbackPanel);
    form.add(new EditContractInputPanel("editContract", model));
    form.add(
        new EditContractAdditionalAttributesPanel(
            "editAdditionalAttributes",
            model.map(ContractBaseData::getContractAttributes),
            feedbackPanel));
    form.add(
        new Button(
            "submitButton",
            () ->
                getString(
                    model.getObject().getContractId() == 0
                        ? "button.save.createmode"
                        : "button.save.editmode")) {
          @Override
          public void onSubmit() {
            try {
              var contractBaseData = model.getObject();
              var id = service.save(contractBaseData);
              contractBaseData.setContractId(id);
              this.success(getString("feedback.success"));
            } catch (DataIntegrityViolationException e) {
              this.error(getString("feedback.error.dataformat.taxrate"));
            } catch (Exception e) {
              this.error(getString("feedback.error"));
            }
          }
        });
    form.add(createBacklink("cancelButton2"));

    add(form);
  }

  private long getContractId() {
    StringValue value = getPageParameters().get("id");
    if (value == null || value.isEmpty() || value.isNull()) {
      return 0L;
    } else {
      return value.toLong();
    }
  }

  public static PageParameters createParameters(long contractId) {
    PageParameters parameters = new PageParameters();
    parameters.add("id", contractId);
    return parameters;
  }
}
