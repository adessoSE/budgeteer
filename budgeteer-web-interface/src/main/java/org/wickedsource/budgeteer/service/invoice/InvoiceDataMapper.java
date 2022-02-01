package org.wickedsource.budgeteer.service.invoice;

import de.adesso.budgeteer.common.old.MoneyUtil;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.persistence.contract.ContractInvoiceField;
import org.wickedsource.budgeteer.persistence.invoice.InvoiceEntity;
import org.wickedsource.budgeteer.persistence.invoice.InvoiceFieldEntity;
import org.wickedsource.budgeteer.service.AbstractMapper;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;
import org.wickedsource.budgeteer.web.components.fileUpload.FileUploadModel;

@Component
public class InvoiceDataMapper extends AbstractMapper<InvoiceEntity, InvoiceBaseData> {

  @Override
  public InvoiceBaseData map(InvoiceEntity entity) {
    return map(entity, null);
  }

  private InvoiceBaseData map(
      InvoiceEntity entity, HashMap<String, DynamicAttributeField> attributeFieldMap) {
    if (entity == null) return null;
    InvoiceBaseData result = new InvoiceBaseData();
    result.setInvoiceId(entity.getId());
    result.setContractName(entity.getContract().getName());
    result.setInvoiceName(entity.getName());
    result.setContractId(entity.getContract().getId());
    result.setSum(entity.getInvoiceSum());
    result.setInternalNumber(entity.getInternalNumber());
    result.setYear(entity.getYear());
    result.setMonth(entity.getMonth());
    result.setPaidDate(entity.getPaidDate());
    result.setDueDate(entity.getDueDate());
    result.setFileUploadModel(
        new FileUploadModel(entity.getFileName(), entity.getFile(), entity.getLink()));
    BigDecimal taxRate = entity.getContract().getTaxRate();
    result.setTaxRate(taxRate);
    result.setSum_gross(MoneyUtil.getMoneyWithTaxes(result.getSum(), taxRate));
    result.setTaxAmount(MoneyUtil.getTaxAmount(result.getSum(), taxRate));

    HashMap<String, DynamicAttributeField> dynamicAttributeFieldMap =
        new HashMap<String, DynamicAttributeField>();
    if (attributeFieldMap != null) {
      Iterator it = attributeFieldMap.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry pair = (Map.Entry) it.next();
        dynamicAttributeFieldMap.put(
            (String) pair.getKey(), new DynamicAttributeField((String) pair.getKey(), ""));
      }
    } else {
      if (entity.getContract().getInvoiceFields() != null) {
        for (ContractInvoiceField contractInvoiceField : entity.getContract().getInvoiceFields()) {
          dynamicAttributeFieldMap.put(
              contractInvoiceField.getFieldName(),
              new DynamicAttributeField(contractInvoiceField.getFieldName(), ""));
        }
      }
    }
    for (InvoiceFieldEntity fieldEntity : entity.getDynamicFields()) {
      dynamicAttributeFieldMap.put(
          fieldEntity.getField().getFieldName(),
          new DynamicAttributeField(fieldEntity.getField().getFieldName(), fieldEntity.getValue()));
    }
    result.setDynamicInvoiceFields(
        new ArrayList<DynamicAttributeField>(dynamicAttributeFieldMap.values()));

    return result;
  }

  /**
   * maps a list of Invoice entities.
   *
   * @param entityList
   * @param differentProjects if true, the method will take care that all DTOs have the same
   *     attributes. This is just relevant if you're trying to map entities with different contracts
   *     and due to this fact, with different attributes.
   * @return
   */
  public List<InvoiceBaseData> map(List<InvoiceEntity> entityList, boolean differentProjects) {
    List<InvoiceBaseData> result = new LinkedList<InvoiceBaseData>();
    HashMap<String, DynamicAttributeField> attributeFieldMap =
        new HashMap<String, DynamicAttributeField>();
    if (differentProjects) {
      for (InvoiceEntity entity : entityList) {
        for (ContractInvoiceField contractInvoiceField : entity.getContract().getInvoiceFields()) {
          attributeFieldMap.put(
              contractInvoiceField.getFieldName(),
              new DynamicAttributeField(contractInvoiceField.getFieldName(), ""));
        }
      }
    }
    for (InvoiceEntity entity : entityList) {
      result.add(map(entity, attributeFieldMap));
    }
    return result;
  }
}
