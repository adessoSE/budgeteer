package org.wickedsource.budgeteer.web.pages.contract.edit.form;

import static org.apache.wicket.model.LambdaModel.of;

import de.adesso.budgeteer.persistence.contract.ContractEntity;
import java.math.BigDecimal;
import java.util.Arrays;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.springframework.dao.DataIntegrityViolationException;
import org.wickedsource.budgeteer.service.DateUtil;
import org.wickedsource.budgeteer.service.contract.ContractBaseData;
import org.wickedsource.budgeteer.service.contract.ContractService;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.components.daterange.DateInputField;
import org.wickedsource.budgeteer.web.components.fileUpload.CustomFileUpload;
import org.wickedsource.budgeteer.web.components.money.MoneyTextField;

public class EditContractForm extends Form<ContractBaseData> {

  @SpringBean private ContractService service;

  private WebMarkupContainer table;

  private String submitButtonTextKey;

  private TextField<String> newAttributeField;
  private CustomFeedbackPanel feedbackPanel;

  public EditContractForm(String id) {
    this(id, null, "button.save.createmode");
  }

  public EditContractForm(String id, IModel<ContractBaseData> model) {
    this(id, model, "button.save.editmode");
  }

  private EditContractForm(String id, IModel<ContractBaseData> model, String submitButtonTextKey) {
    super(id);
    if (model != null) {
      super.setDefaultModel(model);
    } else {
      super.setDefaultModel(
          Model.of(service.getEmptyContractModel(BudgeteerSession.get().getProjectId())));
    }
    Injector.get().inject(this);
    this.submitButtonTextKey = submitButtonTextKey;
    addComponents();
  }

  private void addComponents() {
    feedbackPanel = new CustomFeedbackPanel("feedback");
    feedbackPanel.setOutputMarkupId(true);
    add(feedbackPanel);

    TextField<String> nameTextfield =
        new TextField<>(
            "contractName",
            of(getModel(), ContractBaseData::getContractName, ContractBaseData::setContractName));
    nameTextfield.setRequired(true);
    add(nameTextfield);

    TextField<String> internalNumberTextfield =
        new TextField<>(
            "internalNumber",
            of(
                getModel(),
                ContractBaseData::getInternalNumber,
                ContractBaseData::setInternalNumber));
    internalNumberTextfield.setRequired(true);
    add(internalNumberTextfield);

    MoneyTextField budgetTextfield =
        new MoneyTextField(
            "budget", of(getModel(), ContractBaseData::getBudget, ContractBaseData::setBudget));
    budgetTextfield.setRequired(true);
    add(budgetTextfield);

    var taxrateTextfield =
        new TextField<>(
                "taxrate",
                of(getModel(), ContractBaseData::getTaxRate, ContractBaseData::setTaxRate))
            .setType(BigDecimal.class);
    taxrateTextfield.setRequired(true);
    taxrateTextfield.add(RangeValidator.minimum(BigDecimal.ZERO));
    add(taxrateTextfield);

    if (getModelObject().getStartDate() == null) {
      getModelObject().setStartDate(DateUtil.getBeginOfYear());
    }
    DateInputField startDateInputField =
        new DateInputField(
            "startDate",
            of(getModel(), ContractBaseData::getStartDate, ContractBaseData::setStartDate),
            DateInputField.DROP_LOCATION.UP);
    startDateInputField.setRequired(true);
    add(startDateInputField);

    add(
        new DropDownChoice<ContractEntity.ContractType>(
            "type",
            of(getModel(), ContractBaseData::getType, ContractBaseData::setType),
            Arrays.asList(ContractEntity.ContractType.values()),
            new EnumChoiceRenderer<ContractEntity.ContractType>(this)));

    final CustomFileUpload fileUpload =
        new CustomFileUpload(
            "fileUpload",
            of(getModel(), ContractBaseData::getFileModel, ContractBaseData::setFileModel));
    add(fileUpload);

    table = new WebMarkupContainer("attributeTable");
    table.setOutputMarkupId(true);
    table.setOutputMarkupPlaceholderTag(true);
    table.add(
        new ListView<>(
            "contractAttributes", getModel().map(ContractBaseData::getContractAttributes)) {
          @Override
          protected void populateItem(ListItem<DynamicAttributeField> item) {
            item.add(
                new Label("attributeTitle", item.getModel().map(DynamicAttributeField::getName)));
            item.add(
                new TextField<>(
                    "attributeValue",
                    of(
                        item.getModel(),
                        DynamicAttributeField::getValue,
                        DynamicAttributeField::setValue)));
          }
        });
    add(table);
    newAttributeField = new TextField<String>("nameOfNewAttribute", Model.of(" "));
    newAttributeField.setOutputMarkupId(true);
    add(newAttributeField);
    Button addAttribute =
        new AjaxButton("addAttribute") {
          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            if (newAttributeField.getModelObject() != null) {
              ((ContractBaseData) getForm().getModelObject())
                  .getContractAttributes()
                  .add(new DynamicAttributeField(newAttributeField.getModelObject(), ""));
              target.add(table, newAttributeField, feedbackPanel);
            } else {
              this.error(getString("feedback.error.nameEmpty"));
              target.add(feedbackPanel);
            }
          }
        };
    addAttribute.setOutputMarkupId(true);
    add(addAttribute);
    add(
        new AjaxButton("save", new StringResourceModel(submitButtonTextKey)) {
          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            try {
              ((ContractBaseData) getForm().getModelObject())
                  .getFileModel()
                  .setFile(fileUpload.getFile());
              ((ContractBaseData) getForm().getModelObject())
                  .getFileModel()
                  .setFileName(fileUpload.getFileName());
              ((ContractBaseData) getForm().getModelObject())
                  .setContractId(service.save((ContractBaseData) getForm().getModelObject()));
              if (submitButtonTextKey.equals("button.save.createmode")) {
                submitButtonTextKey = "button.save.editmode";
                this.setDefaultModel(new StringResourceModel(submitButtonTextKey));
                target.add(this);
                this.success(getString("feedback.success.creation"));
              } else {
                this.success(getString("feedback.success"));
              }
            } catch (DataIntegrityViolationException e) {
              this.error(getString("feedback.error.dataformat.taxrate"));
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
        }.setOutputMarkupId(true));
  }
}
