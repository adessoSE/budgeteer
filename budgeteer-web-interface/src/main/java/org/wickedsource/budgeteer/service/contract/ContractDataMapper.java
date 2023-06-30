package org.wickedsource.budgeteer.service.contract;

import de.adesso.budgeteer.common.old.MoneyUtil;
import de.adesso.budgeteer.persistence.budget.BudgetEntity;
import de.adesso.budgeteer.persistence.contract.ContractEntity;
import de.adesso.budgeteer.persistence.contract.ContractFieldEntity;
import de.adesso.budgeteer.persistence.contract.ContractRepository;
import de.adesso.budgeteer.persistence.project.ProjectContractField;
import java.util.*;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.service.AbstractMapper;
import org.wickedsource.budgeteer.service.DateUtil;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;
import org.wickedsource.budgeteer.web.components.fileUpload.FileUploadModel;

@Component
public class ContractDataMapper extends AbstractMapper<ContractEntity, ContractBaseData> {

  @Autowired private ContractRepository contractRepository;

  @Override
  public ContractBaseData map(ContractEntity entity) {
    if (entity == null) return null;
    ContractBaseData result = new ContractBaseData();
    result.setContractName(entity.getName());
    result.setContractId(entity.getId());
    result.setBudget(entity.getBudget());
    result.setBudgetLeft(
        toMoneyNullsafe(contractRepository.getBudgetLeftByContractId(entity.getId())));
    result.setBudgetSpent(
        toMoneyNullsafe(contractRepository.getSpentBudgetByContractId(entity.getId())));
    result.setInternalNumber(entity.getInternalNumber());
    result.setProjectId(entity.getProject().getId());
    result.setType(entity.getType());
    result.setStartDate(DateUtil.toDate(entity.getStartDate()));
    result.setFileModel(
        new FileUploadModel(entity.getFileName(), entity.getFile(), entity.getLink()));
    result.setTaxRate(entity.getTaxRate());

    Map<String, DynamicAttributeField> contractAttributes = new HashMap<>();
    for (ProjectContractField projectContractField : entity.getProject().getContractFields()) {
      contractAttributes.put(
          projectContractField.getFieldName(),
          new DynamicAttributeField(projectContractField.getFieldName(), ""));
    }
    for (ContractFieldEntity fieldEntity : entity.getContractFields()) {
      contractAttributes.put(
          fieldEntity.getField().getFieldName(),
          new DynamicAttributeField(fieldEntity.getField().getFieldName(), fieldEntity.getValue()));
    }
    result.setContractAttributes(new ArrayList<DynamicAttributeField>(contractAttributes.values()));

    result.setBelongingBudgets(new LinkedList<BudgetBaseData>());
    for (BudgetEntity budgetEntity : entity.getBudgets()) {
      result
          .getBelongingBudgets()
          .add(new BudgetBaseData(budgetEntity.getId(), budgetEntity.getName()));
    }

    return result;
  }

  public List<ContractBaseData> map(List<ContractEntity> entityList) {
    List<ContractBaseData> result = new LinkedList<ContractBaseData>();
    for (ContractEntity entity : entityList) {
      result.add(map(entity));
    }
    return result;
  }

  private Money toMoneyNullsafe(Double cents) {
    if (cents == null) {
      return MoneyUtil.createMoneyFromCents(0l);
    } else {
      return MoneyUtil.createMoneyFromCents(Math.round(cents));
    }
  }
}
