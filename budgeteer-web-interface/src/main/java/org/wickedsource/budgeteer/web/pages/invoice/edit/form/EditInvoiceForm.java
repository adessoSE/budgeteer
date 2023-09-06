package org.wickedsource.budgeteer.web.pages.invoice.edit.form;

import static org.apache.wicket.model.LambdaModel.of;

import java.util.Arrays;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.DateUtil;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;
import org.wickedsource.budgeteer.service.invoice.InvoiceBaseData;
import org.wickedsource.budgeteer.service.invoice.InvoiceService;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.components.daterange.DateInputField;
import org.wickedsource.budgeteer.web.components.fileUpload.CustomFileUpload;
import org.wickedsource.budgeteer.web.components.money.MoneyTextField;
import org.wickedsource.budgeteer.web.components.monthRenderer.MonthRenderer;

public class EditInvoiceForm extends Form<InvoiceBaseData> {

  @SpringBean private InvoiceService service;

  private String nameOfAttribute = "";

  private WebMarkupContainer table;

  private TextField<String> newAttributeField;
  private CustomFeedbackPanel feedbackPanel;

  public EditInvoiceForm(String id, final long contractId) {
    super(id);
    super.setDefaultModel(Model.of(service.getEmptyInvoiceModel(contractId)));
    addComponents();
  }

  public EditInvoiceForm(String id, IModel<InvoiceBaseData> model) {
    super(id, model);
    Injector.get().inject(this);
    addComponents();
  }

  private void addComponents() {
    feedbackPanel = new CustomFeedbackPanel("feedback");
    feedbackPanel.setOutputMarkupId(true);
    add(feedbackPanel);

    TextField<String> invoiceNameTextfield =
        new TextField<String>(
            "invoiceName",
            of(getModel(), InvoiceBaseData::getInvoiceName, InvoiceBaseData::setInvoiceName));
    invoiceNameTextfield.setRequired(true);
    add(invoiceNameTextfield);

    TextField<String> nameTextfield =
        new TextField<String>(
            "contractName",
            of(getModel(), InvoiceBaseData::getContractName, InvoiceBaseData::setContractName));
    nameTextfield.setEnabled(false);
    add(nameTextfield);

    TextField<String> internalNumberTextfield =
        new TextField<String>(
            "internalNumber",
            of(getModel(), InvoiceBaseData::getInternalNumber, InvoiceBaseData::setInternalNumber));
    internalNumberTextfield.setRequired(true);
    add(internalNumberTextfield);

    MoneyTextField budgetTextfield =
        new MoneyTextField("sum", of(getModel(), InvoiceBaseData::getSum, InvoiceBaseData::setSum));
    budgetTextfield.setRequired(true);
    add(budgetTextfield);

    add(
        new DropDownChoice<Integer>(
            "year",
            of(getModel(), InvoiceBaseData::getYear, InvoiceBaseData::setYear),
            DateUtil.getCurrentYears(5)));
    List<Integer> monthList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
    add(
        new DropDownChoice<Integer>(
            "month",
            of(getModel(), InvoiceBaseData::getMonth, InvoiceBaseData::setMonth),
            monthList,
            new MonthRenderer()));

    add(
        new DateInputField(
            "paidDate",
            of(getModel(), InvoiceBaseData::getPaidDate, InvoiceBaseData::setPaidDate)));
    add(
        new DateInputField(
            "dueDate", of(getModel(), InvoiceBaseData::getDueDate, InvoiceBaseData::setDueDate)));

    final CustomFileUpload fileUpload =
        new CustomFileUpload(
            "fileUpload",
            of(
                getModel(),
                InvoiceBaseData::getFileUploadModel,
                InvoiceBaseData::setFileUploadModel));
    add(fileUpload);

    table = new WebMarkupContainer("attributeTable");
    table.setOutputMarkupId(true);
    table.setOutputMarkupPlaceholderTag(true);
    table.add(
        new ListView<DynamicAttributeField>(
            "invoiceAttributes", getModel().map(InvoiceBaseData::getDynamicInvoiceFields)) {
          @Override
          protected void populateItem(ListItem<DynamicAttributeField> item) {
            item.add(
                new Label("attributeTitle", item.getModel().map(DynamicAttributeField::getName)));
            item.add(
                new TextField<String>(
                    "attributeValue",
                    of(
                        item.getModel(),
                        DynamicAttributeField::getValue,
                        DynamicAttributeField::setValue)));
          }
        });
    add(table);
    newAttributeField = new TextField<String>("nameOfNewAttribute", Model.of(nameOfAttribute));
    newAttributeField.setOutputMarkupId(true);
    add(newAttributeField);
    AjaxButton addAttributeButton =
        new AjaxButton("addAttribute") {
          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            if (newAttributeField.getModelObject() != null) {
              ((InvoiceBaseData) getForm().getModelObject())
                  .getDynamicInvoiceFields()
                  .add(new DynamicAttributeField(newAttributeField.getModelObject(), ""));
              target.add(table, newAttributeField, feedbackPanel);
            } else {
              this.error(getString("feedback.error.nameEmpty"));
              target.add(feedbackPanel);
            }
          }

          @Override
          protected void onError(AjaxRequestTarget target) {
            target.add(feedbackPanel);
          }
        };
    add(addAttributeButton);
    add(
        new AjaxButton("save") {
          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            try {
              ((InvoiceBaseData) getForm().getModelObject())
                  .getFileUploadModel()
                  .setFile(fileUpload.getFile());
              ((InvoiceBaseData) getForm().getModelObject())
                  .getFileUploadModel()
                  .setFileName(fileUpload.getFileName());
              ((InvoiceBaseData) getForm().getModelObject())
                  .setInvoiceId(service.save((InvoiceBaseData) getForm().getModelObject()));
              this.success(getString("feedback.success"));
            } catch (Exception e) {
              e.printStackTrace();
              this.error(getString("feedback.error"));
            }
            target.add(feedbackPanel);
          }

          @Override
          protected void onError(AjaxRequestTarget target) {
            target.add(feedbackPanel);
          }
        });
  }
}
