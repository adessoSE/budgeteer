package org.wickedsource.budgeteer.web.pages.contract.edit.form;

import static org.apache.wicket.model.LambdaModel.of;

import de.adesso.budgeteer.persistence.contract.ContractEntity;
import java.math.BigDecimal;
import java.util.Arrays;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.RangeValidator;
import org.wickedsource.budgeteer.service.DateUtil;
import org.wickedsource.budgeteer.service.contract.ContractBaseData;
import org.wickedsource.budgeteer.web.components.daterange.DateInputField;
import org.wickedsource.budgeteer.web.components.fileUpload.CustomFileUpload;
import org.wickedsource.budgeteer.web.components.money.MoneyTextField;

public class EditContractInputPanel extends GenericPanel<ContractBaseData> {

  public EditContractInputPanel(String id, IModel<ContractBaseData> model) {
    super(id, model);

    var form = new Form<>("form", model);
    form.add(
        new RequiredTextField<>(
            "contractName",
            of(
                form.getModel(),
                ContractBaseData::getContractName,
                ContractBaseData::setContractName)));
    form.add(
        new RequiredTextField<>(
            "internalNumber",
            of(
                form.getModel(),
                ContractBaseData::getInternalNumber,
                ContractBaseData::setInternalNumber)));
    form.add(
        new MoneyTextField(
                "budget",
                of(form.getModel(), ContractBaseData::getBudget, ContractBaseData::setBudget))
            .setRequired(true));
    form.add(
        new TextField<>(
                "taxrate",
                of(form.getModel(), ContractBaseData::getTaxRate, ContractBaseData::setTaxRate),
                BigDecimal.class)
            .setRequired(true)
            .add(RangeValidator.minimum(BigDecimal.ZERO)));
    if (getModelObject().getStartDate() == null) {
      getModelObject().setStartDate(DateUtil.getBeginOfYear());
    }
    form.add(
        new DateInputField(
                "startDate",
                of(form.getModel(), ContractBaseData::getStartDate, ContractBaseData::setStartDate),
                DateInputField.DROP_LOCATION.UP)
            .setRequired(true));
    form.add(
        new DropDownChoice<>(
            "type",
            of(form.getModel(), ContractBaseData::getType, ContractBaseData::setType),
            Arrays.asList(ContractEntity.ContractType.values()),
            new EnumChoiceRenderer<>(this)));
    form.add(
        new CustomFileUpload(
            "fileUpload",
            of(form.getModel(), ContractBaseData::getFileModel, ContractBaseData::setFileModel)));

    add(form);
  }
}
