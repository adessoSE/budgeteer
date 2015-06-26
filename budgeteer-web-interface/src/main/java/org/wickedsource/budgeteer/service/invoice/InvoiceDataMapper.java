package org.wickedsource.budgeteer.service.invoice;

import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.persistence.contract.ContractInvoiceField;
import org.wickedsource.budgeteer.persistence.invoice.InvoiceEntity;
import org.wickedsource.budgeteer.persistence.invoice.InvoiceFieldEntity;
import org.wickedsource.budgeteer.service.AbstractMapper;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;

import java.util.*;

@Component
public class InvoiceDataMapper extends AbstractMapper<InvoiceEntity, InvoiceBaseData> {

    @Override
    public InvoiceBaseData map(InvoiceEntity entity) {
        return map(entity, null);
    }

    public InvoiceBaseData map(InvoiceEntity entity,  Map<String, DynamicAttributeField> attributeFieldMap) {
        if(entity == null)
            return null;
        InvoiceBaseData result = new InvoiceBaseData();
        result.setInvoiceId(entity.getId());
        result.setContractName(entity.getContract().getName());
        result.setInvoiceName(entity.getName());
        result.setContractId(entity.getContract().getId());
        result.setSum(entity.getSum());
        result.setInternalNumber(entity.getInternalNumber());
        result.setYear(entity.getYear());
        result.setMonth(entity.getMonth());
        result.setPaid(entity.isPaid());

        if(attributeFieldMap == null) {
            attributeFieldMap = new HashMap<String, DynamicAttributeField>();
        }
        for(InvoiceFieldEntity fieldEntity : entity.getDynamicFields()){
            attributeFieldMap.put(fieldEntity.getField().getFieldName(), new DynamicAttributeField(fieldEntity.getField().getFieldName(), fieldEntity.getValue()));
        }
        result.setDynamicInvoiceFields(new ArrayList<DynamicAttributeField>(attributeFieldMap.values()));

        return result;
    }

    /**
     * maps a list of Invoice entities.
     * @param entityList
     * @param differentProjects if true, the method will take care that all DTOs have the same attributes. This is just relevant if you're trying to map entities  with different contracts and due to this fact, with different attributes.
     * @return
     */
        public List<InvoiceBaseData> map(List<InvoiceEntity> entityList, boolean differentProjects){
            List<InvoiceBaseData> result = new LinkedList<InvoiceBaseData>();
            Map<String, DynamicAttributeField> attributeFieldMap = new HashMap<String, DynamicAttributeField>();
            if(differentProjects){
                for(InvoiceEntity entity : entityList){
                    for (ContractInvoiceField contractInvoiceField : entity.getContract().getInvoiceFields()) {
                        attributeFieldMap.put(contractInvoiceField.getFieldName(), new DynamicAttributeField(contractInvoiceField.getFieldName(), ""));
                    }
                }
            }
            for(InvoiceEntity entity : entityList){
                result.add(map(entity, attributeFieldMap));
            }
            return result;
        }
 }